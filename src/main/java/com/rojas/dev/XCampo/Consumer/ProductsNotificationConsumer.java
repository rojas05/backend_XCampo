package com.rojas.dev.XCampo.Consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rojas.dev.XCampo.dto.Notifications;
import com.rojas.dev.XCampo.entity.Order;
import com.rojas.dev.XCampo.entity.Product;
import com.rojas.dev.XCampo.service.Interface.FirebaseNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ProductsNotificationConsumer {

    @Autowired
    FirebaseNotificationService firebaseNotificationService;

    @KafkaListener(topics = "product-notifications", groupId = "delivery-group")
    public void consumerOrderEvent(String message){
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            Notifications notification  = mapper.readValue(message, Notifications.class);
            firebaseNotificationService.sendNotifications(notification);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

}
