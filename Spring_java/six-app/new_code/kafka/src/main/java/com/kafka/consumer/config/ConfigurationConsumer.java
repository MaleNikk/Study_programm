package com.kafka.consumer.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

@Configuration
@Slf4j
public class ConfigurationConsumer {

    @Value("${spring.kafka.applicationURI}")
    private String serverPort;

    @Value("${spring.kafka.applicationSendGroupId}")
    private String groupId;

    @Bean
    public ProducerFactory<String, String> producerFactoryReturn() {
        log.info("Create bean producer factory with entity Status.");
        return new DefaultKafkaProducerFactory<>(
                Map.of(
                        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, serverPort,
                        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
                )
        );
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplateConsumer() {
        log.info("Create bean kafkaTemplateConsumer with entity Status.");
        return new KafkaTemplate<>(producerFactoryReturn());
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                Map.of(
                        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serverPort,
                        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
                        ConsumerConfig.GROUP_ID_CONFIG, groupId
                        )
        );
    }
}
