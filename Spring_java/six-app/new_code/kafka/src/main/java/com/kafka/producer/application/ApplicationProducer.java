package com.kafka.producer.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
@ComponentScan(basePackages = "com.kafka.producer")
public class ApplicationProducer {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationProducer.class, args);
    }
}
