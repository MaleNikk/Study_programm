package com.kafka.producer.controller;

import com.kafka.producer.model.OrderEntity;
import com.kafka.producer.service.ProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/kafka/prod")
public class AppProducerController {

    @Autowired
    private ProducerService producerService;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody OrderEntity orderEntity) {
        log.info("Init method send data to kafka at system time: {}", System.currentTimeMillis());
        producerService.sendData(orderEntity);
        return ResponseEntity.ofNullable("Data send to kafka!\n".concat(orderEntity.toString()));
    }
}
