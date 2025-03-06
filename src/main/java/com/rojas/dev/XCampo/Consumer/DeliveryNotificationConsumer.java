package com.rojas.dev.XCampo.Consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rojas.dev.XCampo.dto.Notifications;
import com.rojas.dev.XCampo.entity.DeliveryProduct;
import com.rojas.dev.XCampo.service.Interface.FirebaseNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class DeliveryNotificationConsumer {

    @Autowired
    FirebaseNotificationService firebaseNotificationService;

    @KafkaListener(topics = "delivery-notifications", groupId = "delivery-group") // concurrency = "1": para que un solo consumidor la procese
    public void consumerDeliveryEvent(String message){
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            //DeliveryProduct delivery  = mapper.readValue(message, DeliveryProduct.class);
            Notifications notification  = mapper.readValue(message, Notifications.class);
            System.out.println("Notificacion enviada desde kafka nuevo pedido en " + message);

            firebaseNotificationService.sendNotification(notification);
        } catch (Exception e) {
            System.err.println("❌ Error al consumir la notificación: " + e);
        }
    }

    @KafkaListener(topics = "deliveryUpdate-notification", groupId = "delivery-group")
    public void consumerDeliveryUpdateEvent(String message){
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            DeliveryProduct delivery  = mapper.readValue(message,DeliveryProduct.class);
            System.out.println("notificacion enviada desde kafka pedido " + delivery.getId() + " " + delivery.getState());
        } catch (Exception e) {
            System.err.println(e);
        }
    }

}
