package com.kafka.consumer.listening;

import com.kafka.consumer.mapper.SendDataMapper;
import com.kafka.consumer.model.OrderStatus;
import com.kafka.consumer.model.StatusEntity;
import com.kafka.consumer.service.ConsumerService;
import com.kafka.producer.model.OrderEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

import static com.kafka.consumer.mapper.SendDataMapper.getOrderFromData;

@Component
@Slf4j
public class ApplicationConsumerListener {

    @Autowired
    private ConsumerService consumerService;

    @Value("${spring.kafka.consumerTopic}")
    private String consumerStatusTopic;

    @KafkaListener(
            topics = "${spring.kafka.producerTopic}",
            groupId = "${spring.kafka.applicationSendGroupId}"
    )
    public void listenEvent(@Payload String data,
                            @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) UUID key,
                            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                            @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp){
        log.info("Received data: {}", data);
        log.info("Key: {}; Partition: {}; Topic: {}; Timestamp: {}", key,partition,topic,timestamp);
        enableStatus(getOrderFromData(data));
    }

    public void enableStatus(OrderEntity order) {
        log.info("Consumer get data from topic at system time: {}", System.currentTimeMillis());
        consumerService.addOrder(order);
        StatusEntity statusEntity = new StatusEntity();
        statusEntity.setStatus(OrderStatus.CREATED.toString());
        statusEntity.setTime(Instant.now());
        statusEntity.setOrder(order);
        consumerService.sendStatus(consumerStatusTopic, statusEntity);
    }
}
