package com.csdn.event.kafka.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class JsonDeserializer implements Deserializer<Object> {

    private static final Logger log = LoggerFactory.getLogger(JsonDeserializer.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map configs, boolean isKey) {

    }

    @Override
    public Object deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }
        try {
            return objectMapper.readValue(data, Object.class);
        } catch (Exception e) {
            log.error("反序列化消息失败, topic: {}", topic, e);
            throw new RuntimeException("消息反序列化失败", e);
        }
    }

    @Override
    public void close() {
        //nothing to close
    }

}
