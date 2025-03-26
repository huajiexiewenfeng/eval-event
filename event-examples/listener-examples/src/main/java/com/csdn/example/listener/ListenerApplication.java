package com.csdn.example.listener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 事件监听者示例启动类
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.csdn.event", "com.csdn.example"})
public class ListenerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ListenerApplication.class, args);
    }

}