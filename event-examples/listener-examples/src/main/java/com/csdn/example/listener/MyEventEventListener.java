package com.csdn.example.listener;

import com.alibaba.fastjson.JSONObject;
import com.csdn.event.kafka.listener.EvalEventListener;
import com.csdn.example.listener.model.MyEvent;
import org.springframework.stereotype.Component;

@Component
public class MyEventEventListener implements EvalEventListener<MyEvent> {
    @Override
    public void onEvent(MyEvent event) {
        System.out.println("Received event: " + JSONObject.toJSONString(event));
    }
}