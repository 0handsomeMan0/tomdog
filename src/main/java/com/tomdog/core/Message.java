package com.tomdog.core;

import com.tomdog.utils.PropertiesUtil;

/**
 * @author zhouyu
 * @description 消息接口
 **/
public interface Message {

    /**
     * broker服务端地址
     */
    public static String KAFKA_SERVER =PropertiesUtil.readProperty("kafka.server");

}
