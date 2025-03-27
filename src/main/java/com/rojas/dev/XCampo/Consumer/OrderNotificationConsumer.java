package com.rojas.dev.XCampo.Consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rojas.dev.XCampo.dto.Notifications;
import com.rojas.dev.XCampo.service.Interface.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderNotificationConsumer {

    @Autowired
    NotificationService notificationService;

    @Autowired
    ObjectMapper objectMapper;

    /**
     * consumer de kafka para notificaciones de ordenes
     * @param message
     */
    @KafkaListener(topics = "order-notifications", groupId = "delivery-group")
    public void consumerOrderEvent(String message){
        try {
            Notifications notification  = objectMapper.readValue(message, Notifications.class);
            notificationService.sendNotification(notification);
        } catch (Exception e) {
            System.err.println("❌ Error al consumir la notificación en la Orden: " + e);
        }
    }

}
