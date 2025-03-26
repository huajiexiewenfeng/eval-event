package com.csdn.event.kafka.listener;

import com.csdn.event.kafka.model.EvalEventDefinition;
import com.csdn.event.kafka.utils.EvalEventSubclassScannerUtil;
import com.csdn.event.sdk.EvalEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Component
@ConditionalOnProperty(value = "event.kafka.listener.enabled", havingValue = "true")
public class KafkaEvalEventDispatcher<T extends EvalEvent> {

    private static final Logger log = LoggerFactory.getLogger(KafkaEvalEventDispatcher.class);

    @Autowired
    private List<EvalEventListener<T>> listeners;

    @Autowired
    private EventKafkaConsumerFactory eventKafkaConsumerFactory;

    @Value("${event.kafka.base-package:com}")
    private String basePackage;

    @PostConstruct
    public void init() {
        log.info(">>>> KafkaEvalEventListener initialized");
        // 反射方式找到 EvalEvent 和 listener 的映射关系
        try {
            Map<String, EvalEventDefinition> evalEventDefinitionsMap = EvalEventSubclassScannerUtil.getEvalEventDefinitions(basePackage);
            log.info(">>>> KafkaEvalEventListener topicMap: {}", evalEventDefinitionsMap);
            for (EvalEventListener<T> listener : listeners) {
                // 获取监听器的类名
                String cla = listener.getClass().getName();
                if (evalEventDefinitionsMap.containsKey(cla)) {
                    EvalEventDefinition evalEventDefinition = evalEventDefinitionsMap.get(cla);
                    log.info(">>>> KafkaEvalEventListener found listener: {}", evalEventDefinition);
                    EvalEventListenerThread<T> evalEventListenerThread = new EvalEventListenerThread<>(evalEventDefinition, listener, eventKafkaConsumerFactory);
                    evalEventListenerThread.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
