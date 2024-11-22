package com.kafka.producer.service;

import com.kafka.producer.model.OrderEntity;
import com.kafka.producer.storage.ProducerStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ApplicationProducerService implements ProducerService {

    @Autowired
    private ProducerStorage producerStorage;

    public void sendData(OrderEntity order){
        log.info("Service push data to producer topic at system time: {}", System.currentTimeMillis());
        producerStorage.sendData(order);
    }
}
