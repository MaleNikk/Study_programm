package com.kafka.producer.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class OrderEntity {

    private String product;

    private Integer quantity;

    @Override
    public String toString() {
        return "OrderEntity{" +
                "product='" + product + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
