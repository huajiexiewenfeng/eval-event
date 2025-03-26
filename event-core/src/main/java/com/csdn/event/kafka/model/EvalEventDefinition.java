package com.csdn.event.kafka.model;

public class EvalEventDefinition {
    private String topic;
    private Class<?> eventClass;

    public EvalEventDefinition() {
    }

    public EvalEventDefinition(String topic, Class<?> eventClass) {
        this.topic = topic;
        this.eventClass = eventClass;
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
}
