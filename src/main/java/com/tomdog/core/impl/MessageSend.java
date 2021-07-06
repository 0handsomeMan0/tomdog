package com.tomdog.core.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tomdog.common.CommonData;
import com.tomdog.core.Message;
import com.tomdog.entity.Command;
import org.apache.kafka.clients.consumer.StickyAssignor;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author zhouyu
 * @description 消息发送类
 **/
public class MessageSend implements Message {
    private static KafkaProducer<String, String> kafkaProducer=null;
    static {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,Message.KAFKA_SERVER);
        properties.put(ProducerConfig.ACKS_CONFIG,"all");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
        //retry
        properties.put(ProducerConfig.RETRIES_CONFIG,3);
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,true);
        properties.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,1);
        kafkaProducer = new KafkaProducer<>(properties);
    }

    /**
     * 发送消息
     * @param command
     * @return is success
     */
    public static boolean sendCommand(Command command){
        ProducerRecord<String, String> record = new ProducerRecord<>(command.getClass().getSimpleName(),command.getClass().getSimpleName(), JSON.toJSONString(command));
        try {
            kafkaProducer.send(record).get(300, TimeUnit.MILLISECONDS);
            return true;
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            return false;
        }
    }
}
