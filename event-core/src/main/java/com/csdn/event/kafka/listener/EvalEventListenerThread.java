package com.csdn.event.kafka.listener;

import com.csdn.event.kafka.model.EvalEventDefinition;
import com.csdn.event.kafka.utils.ConvertUtil;
import com.csdn.event.sdk.EvalEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class EvalEventListenerThread<T extends EvalEvent> extends Thread {

    private static final Logger log = LoggerFactory.getLogger(EvalEventListenerThread.class);

    private final EvalEventListener<T> evalEventListener;

    private final EventKafkaConsumerFactory eventKafkaConsumerFactory;

    private EvalEventDefinition evalEventDefinition;

    public EvalEventListenerThread(EvalEventDefinition evalEventDefinition, EvalEventListener<T> evalEventListener,
                                   EventKafkaConsumerFactory eventKafkaConsumerFactory) {
        this.evalEventListener = evalEventListener;
        this.evalEventDefinition = evalEventDefinition;
        this.eventKafkaConsumerFactory = eventKafkaConsumerFactory;
    }

    @Override
    public void run() {
        // 1. 创建KafkaConsumer
        KafkaConsumer<String, ?> consumer;
        try {
            consumer = eventKafkaConsumerFactory.buildKafkaConsumer(evalEventListener.getClass());
            List<String> topicList = new ArrayList<>();
            topicList.add(evalEventDefinition.getTopic());
            consumer.subscribe(topicList);
        } catch (Exception e) {
            log.error("KafkaConsumer构造失败", e);
            e.printStackTrace();
            return;
        }
        // 2. 消费消息
        try {
            while (true) {
                try {
                    // 3. 拉取消息
                    ConsumerRecords<String, ?> records = consumer.poll(
                            Duration.ofMillis(500));
                    if (records.isEmpty()) {
                        continue;
                    }
                    // 4. 处理消息
                    dispatch(records);
                    // 5. 使用异步提交规避阻塞
                    consumer.commitAsync();
                } catch (Exception e) {
                    log.error("消息处理异常", e);
                }
            }
        } finally {
            try {
                // 6.最后一次提交使用同步阻塞式提交
                consumer.commitSync();
            } finally {
                consumer.close();
            }
        }
    }

    private void dispatch(ConsumerRecords<String, ?> records) {
        for (ConsumerRecord<String, ?> record : records) {
            Object data = record.value();
            // 这里获取到的 T = linkedHashMap 需要转换成对应的事件类型
            T event = (T) ConvertUtil.convertEvent(data, evalEventDefinition.getEventClass());
            evalEventListener.onEvent(event);
        }
    }

}
