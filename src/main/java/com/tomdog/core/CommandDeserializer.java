package com.tomdog.core;

import com.alibaba.fastjson.JSON;
import com.tomdog.common.CommonData;
import com.tomdog.entity.Command;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author zhouyu
 * @description TODO
 **/
public class CommandDeserializer implements Deserializer<Command> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) { }

    @Override
    public Command deserialize(String topic, byte[] bytes) {
        if (bytes==null||bytes.length==0){
            return new Command();
        }
        if (bytes.length< CommonData.length){
            throw new SerializationException("size of bytes received is too shorter than expected");
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        int length=byteBuffer.getInt();
        byte[] objBytes = new byte[length];
        byteBuffer.get(objBytes);
        Object obj= JSON.parseObject(new String(objBytes, StandardCharsets.UTF_8));
        return new Command(obj);
    }


    @Override
    public void close() { }
}
