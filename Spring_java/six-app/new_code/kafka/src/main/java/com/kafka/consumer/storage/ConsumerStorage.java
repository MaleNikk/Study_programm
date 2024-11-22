package com.kafka.consumer.storage;

import com.kafka.consumer.model.StatusEntity;
import com.kafka.producer.model.OrderEntity;

import java.util.List;

public interface ConsumerStorage {

    void sendStatus(String topic, StatusEntity status);

    void addOrder(OrderEntity order);

    List<OrderEntity> getAllOrders();
}
