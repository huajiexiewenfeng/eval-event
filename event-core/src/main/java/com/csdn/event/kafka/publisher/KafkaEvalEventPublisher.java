package com.csdn.event.kafka.publisher;

import com.csdn.event.kafka.config.EvalKafkaProperties;
import com.csdn.event.sdk.EvalEvent;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Properties;

@Component
@ConditionalOnProperty(value = "event.kafka.publisher.enabled", havingValue = "true")
public class KafkaEvalEventPublisher<T extends EvalEvent> implements EvalEventPublisher<T> {

    private static final Logger log = LoggerFactory.getLogger(KafkaEvalEventPublisher.class);

    @Autowired
    private EvalKafkaProperties kafkaProperties;

    private Producer<String, EvalEvent> producer;

    @PostConstruct
    public void init() {
        try {
            // Initialize the Kafka producer
            Properties props = new Properties();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
            props.put(ProducerConfig.ACKS_CONFIG, kafkaProperties.getProducer().getAcks());
            props.put(ProducerConfig.RETRIES_CONFIG, kafkaProperties.getProducer().getRetries());
            props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaProperties.getProducer().getBatchSize());
            props.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProperties.getProducer().getLingerMs());
            props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, kafkaProperties.getProducer().getBufferMemory());
            props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, kafkaProperties.getProducer().getMaxRequestSize());
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProperties.getKeySerializer());
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProperties.getValueSerializer());
            producer = new KafkaProducer<>(props);
        } catch (Exception e) {
            log.error(">>>> Kafka producer initialization failed", e);
        }
    }

    @Override
    public void publishEvent(T event) {
        producer.send(buildRecord(event), (recordMetadata, e) -> {
            if (e != null) {
                log.error(String.format(">>>> 事件id:%s, topic:%s, 发送失败", event.getId(), event.getTopic()), e);
            } else {
                log.info(">>>> 事件id:{}, topic:{}, 发送成功", event.getId(), event.getTopic());
            }
        });
    }

    private ProducerRecord<String, EvalEvent> buildRecord(EvalEvent t) {
        return new ProducerRecord<>(t.getTopic(), t.getId(), t);
    }

}
