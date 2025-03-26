package com.csdn.event.kafka.listener;

import com.csdn.event.kafka.config.EvalKafkaProperties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EventKafkaConsumerFactory {

    @Autowired
    private EvalKafkaProperties kafkaProperties;

    public KafkaConsumer<String, ?> buildKafkaConsumer(EvalEventListener<?> listener) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, this.getGroupId(listener));
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaProperties.getConsumer().getAutoOffsetReset());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, kafkaProperties.getConsumer().getEnableAutoCommit());
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, kafkaProperties.getConsumer().getSessionTimeout());
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, kafkaProperties.getConsumer().getMaxPollInterval());
        props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, kafkaProperties.getConsumer().getFetchMaxWaitMs());
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, kafkaProperties.getConsumer().getMaxPollRecords());
        props.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, kafkaProperties.getConsumer().getFetchMaxBytes());
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, kafkaProperties.getConsumer().getPartitionFetchMaxBytes());
        props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, kafkaProperties.getConsumer().getFetchMinBytes());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getKeyDeserializer());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getValueDeserializer());
        props.put("spring.json.trusted.packages", "*");
        return new KafkaConsumer<>(props);
    }

    /**
     * 获取消费者组ID
     *
     * @param listener 事件监听器
     * @return 消费者组ID
     */
    private String getGroupId(EvalEventListener<?> listener) {
        return kafkaProperties.getConsumer().getGroupId() + "-" + listener.getClass().getSimpleName();
    }

}