package com.data.loader.simulator.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;


import java.util.Map;

public class JsonSerializer<T> implements Serializer<T> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonSerializer(){}
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
//        Serializer.super.configure(configs, isKey);
    }

    @Override
    public byte[] serialize(String topic, T data) {
        if(data==null){
            return null;
        }
        try{
            return objectMapper.writeValueAsString(data).getBytes();
        }
        catch (Exception e){
            throw  new SecurityException("error serializing==>"+e);
        }
    }

//    @Override
//    public byte[] serialize(String topic, Headers headers, T data) {
//        return Serializer.super.serialize(topic, headers, data);
//    }

    @Override
    public void close() {
        Serializer.super.close();
    }
}
