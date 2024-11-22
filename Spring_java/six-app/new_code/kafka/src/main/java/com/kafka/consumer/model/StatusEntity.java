package com.kafka.consumer.model;

import com.kafka.producer.model.OrderEntity;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Data
public class StatusEntity {

    private String status;

    private Instant time;

    private OrderEntity order;
}
