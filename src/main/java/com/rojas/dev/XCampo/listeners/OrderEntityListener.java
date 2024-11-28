package com.rojas.dev.XCampo.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rojas.dev.XCampo.entity.DeliveryProduct;
import com.rojas.dev.XCampo.entity.Order;
import jakarta.persistence.PostPersist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEntityListener {

    private static KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    public  void init(KafkaTemplate<String,String> kafkaTemplate){
        OrderEntityListener.kafkaTemplate = kafkaTemplate;
    }

    @PostPersist
    public void onOrderCreated(Order order){
        try{
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String orderJson = mapper.writeValueAsString(order);
            kafkaTemplate.send("order-notifications", orderJson);
            System.out.println("evento enviado a Kafka");
        } catch (JsonProcessingException e) {
            System.err.println("ERROR ====>" + e);
        }
    }
}
