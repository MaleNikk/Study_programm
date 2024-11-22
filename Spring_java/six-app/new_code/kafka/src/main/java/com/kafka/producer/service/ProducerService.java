package com.kafka.producer.service;

import com.kafka.producer.model.OrderEntity;

public interface ProducerService {

    public void sendData(OrderEntity order);
}
