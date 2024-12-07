package com.rojas.dev.XCampo.Consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rojas.dev.XCampo.entity.Order;
import com.rojas.dev.XCampo.entity.Product;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ProductsNotificationConsumer {

    @KafkaListener(topics = "product-notifications", groupId = "delivery-group")
    public void consumerOrderEvent(String message){
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            Product product  = mapper.readValue(message, Product.class);
            System.out.println("notificacion enviada desde kafka nuevo pedido en " + product.getName());
        } catch (Exception e) {
            System.err.println(e);
        }
    }

}
