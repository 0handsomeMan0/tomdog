package com.tomdog.entity;

import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhouyu
 * @description 滑动节点
 **/
public class Node {
    public static volatile AtomicInteger count=new AtomicInteger(0);
    private Node next;
    private TopicPartition topicPartition;
    private OffsetAndMetadata offsetAndMetadata;

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public static AtomicInteger getCount() {
        return count;
    }

    public TopicPartition getTopicPartition() {
        return topicPartition;
    }

    public void setTopicPartition(TopicPartition topicPartition) {
        this.topicPartition = topicPartition;
    }

    public OffsetAndMetadata getOffsetAndMetadata() {
        return offsetAndMetadata;
    }

    public void setOffsetAndMetadata(OffsetAndMetadata offsetAndMetadata) {
        this.offsetAndMetadata = offsetAndMetadata;
    }

    public static final class NodeBuilder {
        private TopicPartition topicPartition;
        private OffsetAndMetadata offsetAndMetadata;

        private NodeBuilder() {
        }

        public static NodeBuilder builder() {
            return new NodeBuilder();
        }

        public NodeBuilder withCount() {
            return this;
        }

        public NodeBuilder withTopicPartition(TopicPartition topicPartition) {
            this.topicPartition = topicPartition;
            return this;
        }

        public NodeBuilder withOffsetAndMetadata(OffsetAndMetadata offsetAndMetadata) {
            this.offsetAndMetadata = offsetAndMetadata;
            return this;
        }

        public Node build() {
            Node node = new Node();
            node.setTopicPartition(this.topicPartition);
            node.setOffsetAndMetadata(this.offsetAndMetadata);
            return node;
        }
    }
}
