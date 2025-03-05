package com.rojas.dev.XCampo.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rojas.dev.XCampo.dto.Notifications;
import com.rojas.dev.XCampo.entity.DeliveryProduct;
import com.rojas.dev.XCampo.entity.Product;
import com.rojas.dev.XCampo.enumClass.DeliveryProductState;
import com.rojas.dev.XCampo.enumClass.UserRole;
import com.rojas.dev.XCampo.event.PersistCreatedEvent;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class DeliveryEntityListener {

    private static ApplicationEventPublisher eventPublisher;

    @Autowired
    public void setEventPublisher(ApplicationEventPublisher eventPublisher) {
        DeliveryEntityListener.eventPublisher = eventPublisher;
    }

    @PostPersist
    public void onProductCreated(DeliveryProduct delivery) {
        System.out.println("📩 Disparando evento de notificación para delivery...");
        Notifications notification = new Notifications(UserRole.DELIVERYMAN, "Nuevo delivery", delivery.getDate().toString(), null,delivery.getID());
        eventPublisher.publishEvent(new PersistCreatedEvent(notification));
    }


}
