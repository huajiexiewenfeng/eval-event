package com.csdn.event.sdk;

import java.io.Serializable;
import java.util.UUID;

public abstract class EvalEvent implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 事件ID 对应 Kafka message 的 key
     */
    private String id = UUID.randomUUID().toString();

    public abstract String getTopic();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
