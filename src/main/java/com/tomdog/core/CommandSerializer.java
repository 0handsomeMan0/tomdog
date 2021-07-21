package com.tomdog.core;

import com.alibaba.fastjson.JSON;
import com.tomdog.common.CommonData;
import com.tomdog.entity.Command;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author zhouyu
 * @description 序列化command
 **/
public class CommandSerializer implements Serializer<Command> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public byte[] serialize(String topic, Command command) {
        if (command==null){
            return null;
        }
        byte[] param=null;
        if (command.getParam()!=null){
            param=JSON.toJSONString(command.getParam()).getBytes(StandardCharsets.UTF_8);
            byte[] simpleName = command.getParam().getClass().getSimpleName().getBytes(StandardCharsets.UTF_8);
            ByteBuffer byteBuffer = ByteBuffer.allocate(CommonData.length + param.length+CommonData.length+simpleName.length);
            byteBuffer.putInt(param.length);
            byteBuffer.put(param);
            byteBuffer.putInt(simpleName.length);
            byteBuffer.put(simpleName);
            return byteBuffer.array();
        }
        return new byte[0];
    }

    @Override
    public void close() {}
}
