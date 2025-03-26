package com.csdn.event.kafka.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ConfigurationProperties(prefix = "eval.kafka")
public class EvalKafkaProperties {

    private List<String> bootstrapServers = new ArrayList<String>(
            Collections.singletonList("localhost:9092"));

    private String keyDeserializer = "org.apache.kafka.common.serialization.StringDeserializer";

    private String valueDeserializer = "com.csdn.event.kafka.serialization.JsonDeserializer";

    private String keySerializer = "org.apache.kafka.common.serialization.StringSerializer";

    private String valueSerializer = "com.csdn.event.kafka.serialization.JsonSerializer";

    private Producer producer = new Producer();

    private Consumer consumer = new Consumer();

    public static class Consumer {

        private String groupId = "eval-consumer-group";

        private String autoOffsetReset = "earliest";

        private String sessionTimeout = "30000";

        private String maxPollInterval = "60000";

        private String fetchMaxWaitMs = "200";

        private String maxPollRecords = "100";

        private String fetchMaxBytes = "33554432";

        private String partitionFetchMaxBytes = "1048576";

        private String fetchMinBytes = "10485760";

        private String enableAutoCommit = "false";

        public Consumer() {
        }

        public String getAutoOffsetReset() {
            return autoOffsetReset;
        }

        public void setAutoOffsetReset(String autoOffsetReset) {
            this.autoOffsetReset = autoOffsetReset;
        }

        public String getFetchMaxBytes() {
            return fetchMaxBytes;
        }

        public void setFetchMaxBytes(String fetchMaxBytes) {
            this.fetchMaxBytes = fetchMaxBytes;
        }

        public String getFetchMaxWaitMs() {
            return fetchMaxWaitMs;
        }

        public void setFetchMaxWaitMs(String fetchMaxWaitMs) {
            this.fetchMaxWaitMs = fetchMaxWaitMs;
        }

        public String getFetchMinBytes() {
            return fetchMinBytes;
        }

        public void setFetchMinBytes(String fetchMinBytes) {
            this.fetchMinBytes = fetchMinBytes;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getMaxPollInterval() {
            return maxPollInterval;
        }

        public void setMaxPollInterval(String maxPollInterval) {
            this.maxPollInterval = maxPollInterval;
        }

        public String getMaxPollRecords() {
            return maxPollRecords;
        }

        public void setMaxPollRecords(String maxPollRecords) {
            this.maxPollRecords = maxPollRecords;
        }

        public String getPartitionFetchMaxBytes() {
            return partitionFetchMaxBytes;
        }

        public void setPartitionFetchMaxBytes(String partitionFetchMaxBytes) {
            this.partitionFetchMaxBytes = partitionFetchMaxBytes;
        }

        public String getSessionTimeout() {
            return sessionTimeout;
        }

        public void setSessionTimeout(String sessionTimeout) {
            this.sessionTimeout = sessionTimeout;
        }

        public String getEnableAutoCommit() {
            return enableAutoCommit;
        }

        public void setEnableAutoCommit(String enableAutoCommit) {
            this.enableAutoCommit = enableAutoCommit;
        }
    }

    public static class Producer {

        private String acks = "all";

        private int retries = 5;

        private int batchSize = 16384;

        private int lingerMs = 1;

        private int bufferMemory = 33554432;

        private int maxRequestSize = 10485760;

        public String getAcks() {
            return acks;
        }

        public void setAcks(String acks) {
            this.acks = acks;
        }

        public int getBatchSize() {
            return batchSize;
        }

        public void setBatchSize(int batchSize) {
            this.batchSize = batchSize;
        }

        public int getBufferMemory() {
            return bufferMemory;
        }

        public void setBufferMemory(int bufferMemory) {
            this.bufferMemory = bufferMemory;
        }

        public int getLingerMs() {
            return lingerMs;
        }

        public void setLingerMs(int lingerMs) {
            this.lingerMs = lingerMs;
        }

        public int getMaxRequestSize() {
            return maxRequestSize;
        }

        public void setMaxRequestSize(int maxRequestSize) {
            this.maxRequestSize = maxRequestSize;
        }

        public int getRetries() {
            return retries;
        }

        public void setRetries(int retries) {
            this.retries = retries;
        }
    }

    public EvalKafkaProperties() {
    }

    public List<String> getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(List<String> bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public Producer getProducer() {
        return producer;
    }

    public void setProducer(Producer producer) {
        this.producer = producer;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public String getKeyDeserializer() {
        return keyDeserializer;
    }

    public void setKeyDeserializer(String keyDeserializer) {
        this.keyDeserializer = keyDeserializer;
    }

    public String getValueDeserializer() {
        return valueDeserializer;
    }

    public void setValueDeserializer(String valueDeserializer) {
        this.valueDeserializer = valueDeserializer;
    }

    public String getKeySerializer() {
        return keySerializer;
    }

    public void setKeySerializer(String keySerializer) {
        this.keySerializer = keySerializer;
    }

    public String getValueSerializer() {
        return valueSerializer;
    }

    public void setValueSerializer(String valueSerializer) {
        this.valueSerializer = valueSerializer;
    }
}
