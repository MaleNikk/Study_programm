package com.kafka.producer.storage;

import com.kafka.producer.model.OrderEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class AppProducerStorage implements ProducerStorage {

    @Value("${spring.kafka.producerTopic}")
    private String producerTopic;

    @Autowired
    private KafkaTemplate<String, String> templateOrder;

    @Override
    public void sendData(OrderEntity order) {
        log.info("Data add to producer topic at system time: {}", System.currentTimeMillis());
        templateOrder.send(producerTopic, order.toString());
    }
}
