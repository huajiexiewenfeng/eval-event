package com.csdn.example.publisher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 事件推送者示例启动类
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.csdn.event", "com.csdn.example"})
public class PublisherApplication {

    public static void main(String[] args) {
       SpringApplication.run(PublisherApplication.class, args);
    }

}