package com.csdn.event.kafka.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(EvalKafkaProperties.class)
@ComponentScan(basePackages = "com.csdn.event")
public class EvalKafkaConfig {

}
