package com.csdn.event.kafka.annotation;

import com.csdn.event.kafka.listener.EventKafkaConsumerFactory;
import com.csdn.event.kafka.model.EvalEventAnnotationDefinition;
import com.csdn.event.kafka.utils.ConvertUtil;
import com.csdn.event.sdk.EvalEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class EvalEventListenerAnnotationThread<T extends EvalEvent> extends Thread {

    private static final Logger log = LoggerFactory.getLogger(EvalEventListenerAnnotationThread.class);

    private final EventKafkaConsumerFactory eventKafkaConsumerFactory;

    private final EvalEventAnnotationDefinition evalEventAnnotationDefinition;

    private final ApplicationContext applicationContext;

    public EvalEventListenerAnnotationThread(EvalEventAnnotationDefinition evalEventAnnotationDefinition,
                                             EventKafkaConsumerFactory eventKafkaConsumerFactory, ApplicationContext applicationContext) {
        this.evalEventAnnotationDefinition = evalEventAnnotationDefinition;
        this.eventKafkaConsumerFactory = eventKafkaConsumerFactory;
        this.applicationContext = applicationContext;
    }

    @Override
    public void run() {
        // 1. 创建KafkaConsumer
        KafkaConsumer<String, ?> consumer;
        try {
            consumer = eventKafkaConsumerFactory.buildKafkaConsumer(evalEventAnnotationDefinition.getTargetClass());
            List<String> topicList = new ArrayList<>();
            topicList.add(evalEventAnnotationDefinition.getTopic());
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
            try {
                Object data = record.value();
                if (data == null) {
                    log.warn("接收到空消息记录，跳过处理");
                    continue;
                }

                // 转换事件数据为指定类型
                T event = (T) ConvertUtil.convertEvent(data, evalEventAnnotationDefinition.getEventClass());
                if (event == null) {
                    log.error("事件数据转换失败，跳过处理");
                    continue;
                }

                Class<?> targetClass = evalEventAnnotationDefinition.getTargetClass();
                Method method = evalEventAnnotationDefinition.getMethod();

                if (targetClass == null || method == null) {
                    log.error("目标类或方法为空，无法处理事件");
                    continue;
                }

                // 从 Spring 容器获取 bean 实例
                Object target;
                try {
                    // 先尝试从 Spring 容器获取
                    target = applicationContext.getBean(targetClass);
                } catch (NoSuchBeanDefinitionException e) {
                    // 如果容器中没有，尝试创建新实例
                    log.warn("Spring 容器中未找到 {} 的实例，将创建新实例", targetClass.getSimpleName());
                    target = targetClass.getDeclaredConstructor().newInstance();
                    // 可选：如果需要依赖注入，可以在这里自动装配
                    autowireBean(target);
                }
                method.setAccessible(true);
                // 调用目标方法
                method.invoke(target, event);

            } catch (InstantiationException | IllegalAccessException e) {
                log.error("创建目标类实例失败: {}", e.getMessage(), e);
            } catch (NoSuchMethodException e) {
                log.error("找不到目标类的无参构造方法: {}", e.getMessage(), e);
            } catch (InvocationTargetException e) {
                log.error("方法调用失败: {}", e.getTargetException().getMessage(), e.getTargetException());
            } catch (Exception e) {
                log.error("事件处理失败: {}", e.getMessage(), e);
            }
        }
    }

    // 如果需要自动装配，可以添加这个方法
    private void autowireBean(Object bean) {
        AutowireCapableBeanFactory factory = applicationContext.getAutowireCapableBeanFactory();
        factory.autowireBean(bean);
        factory.initializeBean(bean, bean.getClass().getSimpleName());
    }


}
