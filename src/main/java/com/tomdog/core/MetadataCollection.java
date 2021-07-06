package com.tomdog.core;

import com.tomdog.entity.MethodBean;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhouyu
 * @description 元数据集合
 **/
public class MetadataCollection {
    /**
     * 记录所有含有OnCommand注解的method信息
     * String topic
     * MethodBean method信息
     */
    public static ConcurrentHashMap<String, MethodBean> allCommandBean=new ConcurrentHashMap<>();

}
