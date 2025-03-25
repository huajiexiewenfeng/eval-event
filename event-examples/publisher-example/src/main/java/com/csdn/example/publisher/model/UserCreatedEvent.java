package com.csdn.example.publisher.model;

import com.csdn.event.sdk.EvalEvent;

public class UserCreatedEvent extends EvalEvent {

    private User user;

    public UserCreatedEvent() {
    }

    public UserCreatedEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String getTopic() {
        return "user-create-topic";
    }
}
