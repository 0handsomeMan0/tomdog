package com.tomdog.core;
import com.tomdog.core.impl.MessageProcess;
import com.tomdog.entity.Node;
import com.tomdog.entity.Node.NodeBuilder;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.HashMap;

/**
 * @author zhouyu
 * @description offset滑动窗口
 **/
public class OffsetSlidingWindow {
    private final static int slot = Runtime.getRuntime().availableProcessors();
    private static volatile Node headNode=NodeBuilder.builder().build();
    private static volatile HashMap<TopicPartition, OffsetAndMetadata> metadata = new HashMap<>();

    public static Node getHeadNode(){
        return headNode;
    }

    public synchronized static boolean addNode(Node node){
        Node currentNode=headNode;
        while (currentNode.getNext() != null) {
            currentNode = currentNode.getNext();
        }
        if (Node.count.get() >slot){
            return false;
        }else {
            currentNode.setNext(node);
            Node.count.incrementAndGet();
            return true;
        }
    }

    public synchronized static boolean commitOffset(){
        Node node = OffsetSlidingWindow.getHeadNode();
        if (node.getNext()!=null){
            Node next = node.getNext();
            metadata.put(next.getTopicPartition(), next.getOffsetAndMetadata());
            MessageProcess.kafkaConsumer.commitSync(metadata);
            metadata.clear();
            headNode = headNode.getNext();
            Node.count.decrementAndGet();
        }
        return true;
    }

}
