package com.csdn.example.listener;

import com.csdn.event.kafka.listener.EvalEventListener;
import com.csdn.example.listener.model.MyEvent;
import org.springframework.stereotype.Component;

@Component
public class TestEventListener implements EvalEventListener<MyEvent> {
    @Override
    public void onEvent(MyEvent event) {

    }
}