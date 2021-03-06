package com.tomdog.core.impl;

import com.alibaba.fastjson.JSON;
import com.tomdog.common.CommonData;
import com.tomdog.core.CommandDeserializer;
import com.tomdog.core.Message;
import com.tomdog.core.MetadataCollection;
import com.tomdog.core.OffsetSlidingWindow;
import com.tomdog.entity.Command;
import com.tomdog.entity.MethodBean;
import com.tomdog.entity.Node;
import com.tomdog.entity.Node.NodeBuilder;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

/**
 * @author zhouyu
 * @description command处理类
 **/

public class MessageProcess extends Thread implements Message{
    private AtomicBoolean isRunning=new AtomicBoolean(true);
    public static final KafkaConsumer<String,Command> kafkaConsumer;
    private static final ExecutorService executorService;
    private static final int THREAD_NUMBER =Runtime.getRuntime().availableProcessors();

    static {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,Message.KAFKA_SERVER);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, CommandDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, CommonData.TOM_DOG);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,false);
        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, StickyAssignor.class.getName());
        kafkaConsumer=new KafkaConsumer(properties);
        Pattern compile = Pattern.compile("^.*?Command$");
        kafkaConsumer.subscribe(compile);
        executorService=new ThreadPoolExecutor(THREAD_NUMBER,THREAD_NUMBER,0L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<>(),new ThreadPoolExecutor.AbortPolicy());
    }


    @Override
    public void run() {
            while (isRunning.get()) {
                ConsumerRecords records = kafkaConsumer.poll(Duration.ofMillis(100));
                if (!records.isEmpty()) {
                    executorService.submit(new RecordsHandler(records));
                }
            }
    }
}
class RecordsHandler extends Thread{
    private ConsumerRecords<String, Command> records;
    public RecordsHandler(ConsumerRecords<String, Command> records){
        this.records=records;
    }

    @Override
    public void run() {
        for (TopicPartition partition : records.partitions()) {
            List<ConsumerRecord<String, Command>> records = this.records.records(partition);
            long lastConsumerOffSet = records.get(records.size() - 1).offset();
            Node node = NodeBuilder.builder().withTopicPartition(partition).withOffsetAndMetadata(new OffsetAndMetadata(lastConsumerOffSet + 1)).build();
            boolean isSuccess = OffsetSlidingWindow.addNode(node);
            if (isSuccess){
                //process
                for (ConsumerRecord<String, Command> record : records) {
                    String topic = record.topic();
                    Command command = record.value();
                    try {
                        ConcurrentHashMap<String, MethodBean> allCommandBean = MetadataCollection.allCommandBean;
                        for (String key : allCommandBean.keySet()) {
                            if (key.startsWith(topic+"_")){
                                MethodBean methodBean = allCommandBean.get(key);
                                if (command.getParam()==null&&methodBean.getMethod().getParameterTypes().length==0){
                                    methodBean.getMethod().invoke(methodBean.getObject(), null);
                                }else if (methodBean.getMethod().getParameterTypes().length!=0&&methodBean.getMethod().getParameterTypes()[0].getSimpleName().equals(command.getParamType())){
                                    Object o = JSON.parseObject((String) command.getParam(), Class.forName(methodBean.getMethod().getParameterTypes()[0].getName()).newInstance().getClass());
                                    methodBean.getMethod().invoke(methodBean.getObject(),o);
                                }
                            }
                        }
                    } catch (IllegalAccessException | InvocationTargetException | ClassNotFoundException | InstantiationException e) {
                        e.printStackTrace();
                    }
                }
                // commit offset
                OffsetSlidingWindow.commitOffset();
            }
        }
    }
}
