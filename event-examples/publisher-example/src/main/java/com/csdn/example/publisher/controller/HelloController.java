package com.csdn.example.publisher.controller;

import com.csdn.event.kafka.publisher.EvalEventPublisher;
import com.csdn.example.publisher.model.MyEvent;
import com.csdn.example.publisher.model.User;
import com.csdn.example.publisher.model.UserCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/api/eval/event/v1"})
public class HelloController {

    @Autowired
    private EvalEventPublisher<MyEvent> evalEventPublisher;

    @Autowired
    private EvalEventPublisher<UserCreatedEvent> userEventPublisher;

    @GetMapping("/hello")
    public String hello() {
        MyEvent myEvent = new MyEvent();
        myEvent.setMessage("Hello, World!");
        // 发送事件
        evalEventPublisher.publishEvent(myEvent);
        return "ok";
    }

    @GetMapping("/user")
    public String user() {
        UserCreatedEvent userCreatedEvent = new UserCreatedEvent();
        User user = new User("xwf", 18);
        userCreatedEvent.setUser(user);
        // 发送事件
        userEventPublisher.publishEvent(userCreatedEvent);
        return "ok";
    }
}
