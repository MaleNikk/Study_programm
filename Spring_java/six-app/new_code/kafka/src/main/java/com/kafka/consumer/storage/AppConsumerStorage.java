package com.kafka.consumer.storage;

import com.kafka.consumer.model.StatusEntity;
import com.kafka.producer.model.OrderEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class AppConsumerStorage implements ConsumerStorage{

    @Autowired
    private KafkaTemplate<String, String> templateStatus;

    private final List<OrderEntity> orderEntities = new ArrayList<>();

    @Override
    public void sendStatus(String topic, StatusEntity status) {
        log.info("Data pull to consumer at system time: {}", System.currentTimeMillis());
        templateStatus.send(topic, status.toString());
    }

    @Override
    public void addOrder(OrderEntity order) {
        log.info("Consumer get data from topic at system time: {}", System.currentTimeMillis());
        orderEntities.add(order);
    }

    @Override
    public List<OrderEntity> getAllOrders() {
        log.info("Consumer get all data from topic at system time: {}", System.currentTimeMillis());
        return orderEntities;
    }
}
