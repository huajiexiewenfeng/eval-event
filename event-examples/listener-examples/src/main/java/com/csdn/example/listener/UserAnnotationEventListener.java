package com.csdn.example.listener;

import com.alibaba.fastjson.JSONObject;
import com.csdn.event.kafka.annotation.EvalEventListener;
import com.csdn.example.listener.model.UserCreatedEvent;
import org.springframework.stereotype.Component;

@Component
public class UserAnnotationEventListener  {
    @EvalEventListener
    public void onEvent(UserCreatedEvent event) {
        System.out.println("Received event: " + JSONObject.toJSONString(event));
    }
}