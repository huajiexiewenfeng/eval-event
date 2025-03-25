package com.csdn.example.publisher.model;

import com.csdn.event.sdk.EvalEvent;

public class MyEvent extends EvalEvent {

    private String message;

    public MyEvent() {
        super();
    }

    public MyEvent(String message) {
        super();
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getTopic() {
        return "my-test-topic";
    }
}
