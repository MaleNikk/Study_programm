package com.kafka.consumer.service;

import com.kafka.consumer.model.StatusEntity;
import com.kafka.consumer.storage.ConsumerStorage;
import com.kafka.producer.model.OrderEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AppConsumerService implements ConsumerService {

    @Autowired
    private ConsumerStorage consumerStorage;

    @Override
    public void sendStatus(String topic, StatusEntity status) {
        log.info("Service consumer send status to topic at system time: {}", System.currentTimeMillis());
        consumerStorage.sendStatus(topic,status);
    }

    @Override
    public void addOrder(OrderEntity order) {
        log.info("Service consumer get data from topic at system time: {}", System.currentTimeMillis());
        consumerStorage.addOrder(order);
    }

    @Override
    public List<OrderEntity> getAllOrders() {
        log.info("Service consumer get all data from topic at system time: {}", System.currentTimeMillis());
        return consumerStorage.getAllOrders();
    }
}
