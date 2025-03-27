package com.csdn.event.kafka.model;

import java.lang.reflect.Method;

public class EvalEventAnnotationDefinition {
    private String topic;

    // 事件的目标类
    private Class<?> targetClass;

    // 事件的类型
    private Class<?> eventClass;

    private Method method;

    public EvalEventAnnotationDefinition() {
    }

    public EvalEventAnnotationDefinition(String topic, Class<?> targetClass, Class<?> eventClass, Method method) {
        this.topic = topic;
        this.targetClass = targetClass;
        this.eventClass = eventClass;
        this.method = method;
    }

    public Class<?> getEventClass() {
        return eventClass;
    }

    public void setEventClass(Class<?> eventClass) {
        this.eventClass = eventClass;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
    }
}
