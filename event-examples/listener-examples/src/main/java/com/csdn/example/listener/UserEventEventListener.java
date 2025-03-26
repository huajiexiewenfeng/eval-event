package com.csdn.example.listener;

import com.alibaba.fastjson.JSONObject;
import com.csdn.event.kafka.listener.EvalEventListener;
import com.csdn.example.listener.model.MyEvent;
import com.csdn.example.listener.model.UserCreatedEvent;
import org.springframework.stereotype.Component;

@Component
public class UserEventEventListener implements EvalEventListener<UserCreatedEvent> {
    @Override
    public void onEvent(UserCreatedEvent event) {
        System.out.println("Received event: " + JSONObject.toJSONString(event));
    }
}