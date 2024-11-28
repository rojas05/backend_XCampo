package com.rojas.dev.XCampo.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rojas.dev.XCampo.entity.Order;
import com.rojas.dev.XCampo.entity.Product;
import jakarta.persistence.PostPersist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProductEntityListener {

    private static KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    public  void init(KafkaTemplate<String,String> kafkaTemplate){
        ProductEntityListener.kafkaTemplate = kafkaTemplate;
    }

    @PostPersist
    public void onProductCreated(Product product){
        try{
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String orderJson = mapper.writeValueAsString(product);
            kafkaTemplate.send("product-notifications", orderJson);
            System.out.println("evento enviado a Kafka");
        } catch (JsonProcessingException e) {
            System.err.println("ERROR ====>" + e);
        }
    }

}
