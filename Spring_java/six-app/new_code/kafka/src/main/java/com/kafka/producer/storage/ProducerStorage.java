package com.kafka.producer.storage;

import com.kafka.producer.model.OrderEntity;

public interface ProducerStorage {

    public void sendData(OrderEntity order);

}
