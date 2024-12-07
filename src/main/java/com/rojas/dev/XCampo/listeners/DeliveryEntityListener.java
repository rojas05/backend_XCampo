package com.rojas.dev.XCampo.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rojas.dev.XCampo.entity.DeliveryProduct;
import com.rojas.dev.XCampo.enumClass.DeliveryProductState;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class DeliveryEntityListener {

    private static KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    public  void init(KafkaTemplate<String,String> kafkaTemplate){
        DeliveryEntityListener.kafkaTemplate = kafkaTemplate;
    }

    @PostPersist
    public void onDeliveryCreated(DeliveryProduct deliveryProduct){
        try{
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String deliveryJson = mapper.writeValueAsString(deliveryProduct);
            kafkaTemplate.send("delivery-notifications", deliveryJson);
            System.out.println("evento enviado a Kafka");
        } catch (JsonProcessingException e) {
            System.err.println("ERROR ====>" + e);
        }
    }


}
