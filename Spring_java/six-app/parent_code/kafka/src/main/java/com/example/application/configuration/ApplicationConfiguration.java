package com.example.application.configuration;

import com.example.application.model.MessageEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.JacksonUtils;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class ApplicationConfiguration {

    private final String bootstrapServers;

    private final String kafkaMessageGroupId;

    private final ObjectMapper objectMapper;

    public ApplicationConfiguration(
            @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers,
            @Value("${spring.kafka.kafkaMessageGroupId}") String kafkaMessageGroupId) {
        this.bootstrapServers = bootstrapServers;
        this.kafkaMessageGroupId = kafkaMessageGroupId;
        this.objectMapper = JacksonUtils.enhancedObjectMapper();

    }

    @Bean
    public ProducerFactory<String, MessageEntity> producerFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(config, new StringSerializer(), new JsonSerializer<>(objectMapper));

    }

    @Bean
    public ConsumerFactory<String, MessageEntity> consumerFactory() {

        Map<String, Object> configConsumer = new HashMap<>();

        configConsumer.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configConsumer.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configConsumer.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configConsumer.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaMessageGroupId);
        configConsumer.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return new DefaultKafkaConsumerFactory<>(
                configConsumer, new StringDeserializer(), new JsonDeserializer<>(objectMapper)
        );
    }


    @Bean
    public KafkaTemplate<String, MessageEntity> kafkaTemplate(ProducerFactory<String, MessageEntity> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MessageEntity> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, MessageEntity> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        return  factory;
    }
}
