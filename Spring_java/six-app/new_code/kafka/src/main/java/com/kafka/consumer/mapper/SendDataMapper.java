package com.kafka.consumer.mapper;

import com.kafka.producer.model.OrderEntity;

public class SendDataMapper {

    public static OrderEntity getOrderFromData(String data) {
        OrderEntity orderEntity = new OrderEntity();
        String[] dataIncome = data.replaceAll("[{}]","").split(",",2);
        orderEntity.setProduct(dataIncome[0].split("=")[1]);
        orderEntity.setQuantity(Integer.valueOf(dataIncome[1].split("=")[1]));
        return orderEntity;
    }
}
