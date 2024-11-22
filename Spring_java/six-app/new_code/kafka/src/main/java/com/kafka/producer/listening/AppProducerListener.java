package com.kafka.producer.listening;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class AppProducerListener {

    @Value("${spring.kafka.consumerTopic}")
    String consumerTopic;

    @KafkaListener(
            topics = "${spring.kafka.consumerTopic}",
            groupId = "${spring.kafka.applicationStatusGroupId}"
    )
    public void listenEvent(@Payload String status,
                            @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) UUID key,
                            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                            @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp){
        log.info("Received data: {}", status);
        log.info("Key: {}; Partition: {}; Topic: {}; Timestamp: {}", key,partition,topic,timestamp);
    }
}
