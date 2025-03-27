package com.csdn.event.kafka.annotation;

import com.csdn.event.kafka.listener.EventKafkaConsumerFactory;
import com.csdn.event.kafka.model.EvalEventAnnotationDefinition;
import com.csdn.event.sdk.EvalEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class processes beans after their initialization to find methods annotated with
 * {@link EvalEventListener} and register them as event listeners.
 */
@Component
public class EvalEventListenerAnnotationBeanPostProcessor<T extends EvalEvent> implements BeanPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private static final Logger logger = LoggerFactory.getLogger(EvalEventListenerAnnotationBeanPostProcessor.class);

    private final EventKafkaConsumerFactory eventKafkaConsumerFactory;

    public EvalEventListenerAnnotationBeanPostProcessor(EventKafkaConsumerFactory eventKafkaConsumerFactory) {
        this.eventKafkaConsumerFactory = eventKafkaConsumerFactory;
    }

    @Override
    public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(bean);
        Map<Method, Set<EvalEventListener>> annotatedMethods = MethodIntrospector.selectMethods(targetClass,
                (MethodIntrospector.MetadataLookup<Set<EvalEventListener>>) method -> {
                    Set<EvalEventListener> listenerMethods = findListenerAnnotations(method);
                    return (!listenerMethods.isEmpty() ? listenerMethods : null);
                });
        if (annotatedMethods.isEmpty()) {
            // No methods found
            logger.debug("No @EvalEventListener annotations found on bean '{}'", beanName);
        } else {
            // Non-empty set of methods
            for (Map.Entry<Method, Set<EvalEventListener>> entry : annotatedMethods.entrySet()) {
                Method method = entry.getKey();
                for (EvalEventListener listener : entry.getValue()) {
                    processEvalEventListener(listener, method, bean);
                }
            }
            logger.debug(" {} @KafkaListener methods processed on bean '{}'"
                    , annotatedMethods.size(), beanName + ": " + annotatedMethods);
        }
        return bean;
    }

    private void processEvalEventListener(EvalEventListener listener, Method method, Object bean) {
        // topic
        Class<?>[] eventTypeArr = listener.eventType();
        Class<?> eventType = eventTypeArr.length > 0 ? eventTypeArr[0] : null;
        if (eventType == null) {
            // 从方法参数中获取
            eventType = method.getParameterTypes()[0];
        }
        if (eventType == null) {
            throw new IllegalStateException("Event type not specified for @EvalEventListener on method: " + method);
        }
        try {
            Object o = eventType.newInstance();
            if (!(o instanceof EvalEvent)) {
                throw new IllegalStateException("Event type must be a subclass of EvalEvent: " + eventType);
            }
            EvalEvent event = (EvalEvent) o;
            EvalEventAnnotationDefinition evalEventDefinition = new EvalEventAnnotationDefinition(event.getTopic(), bean.getClass(), eventType, method);
            EvalEventListenerAnnotationThread<T> evalEventListenerThread =
                    new EvalEventListenerAnnotationThread<>(evalEventDefinition, eventKafkaConsumerFactory, applicationContext);
            evalEventListenerThread.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /*
     * AnnotationUtils.getRepeatableAnnotations does not look at interfaces
     */
    private Set<EvalEventListener> findListenerAnnotations(Method method) {
        Set<EvalEventListener> listeners = new HashSet<>();
        EvalEventListener ann = AnnotatedElementUtils.findMergedAnnotation(method, EvalEventListener.class);
        if (ann != null) {
            listeners.add(ann);
        }
        return listeners;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}