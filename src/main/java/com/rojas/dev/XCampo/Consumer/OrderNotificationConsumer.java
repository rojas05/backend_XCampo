package com.rojas.dev.XCampo.Consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rojas.dev.XCampo.entity.Order;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class OrderNotificationConsumer {

    @KafkaListener(topics = "order-notifications", groupId = "delivery-group")
    public void consumerOrderEvent(String message){
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            Order order  = mapper.readValue(message, Order.class);
            System.out.println("notificacion enviada desde kafka nuevo pedido en " + order.getMessage());
        } catch (Exception e) {
            System.err.println(e);
        }
    }

}
