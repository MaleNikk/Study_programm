package com.kafka.consumer.controller;

import com.kafka.consumer.service.ConsumerService;
import com.kafka.producer.model.OrderEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/kafka/consumer/")
@Slf4j
public class AppConsumerController {

    @Autowired
    private ConsumerService consumerService;

    @GetMapping("/get/all")
    public ResponseEntity<String> getAll(){
        log.info("Controller init method get all data at system time: {}", System.currentTimeMillis());
        return ResponseEntity.ok("All data from kafka:\n".concat(consumerService.getAllOrders().toString()));
    }
}
