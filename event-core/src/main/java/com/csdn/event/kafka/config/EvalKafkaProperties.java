package com.csdn.event.kafka.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "eval.kafka")
public class EvalKafkaProperties {

    private List<String> bootstrapServers = new ArrayList<String>(
            Collections.singletonList("localhost:9092"));

    private publisher producer = new publisher();

    public static class publisher {

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

    public publisher getProducer() {
        return producer;
    }

    public void setProducer(publisher producer) {
        this.producer = producer;
    }
}
