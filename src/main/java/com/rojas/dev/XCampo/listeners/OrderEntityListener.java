package com.rojas.dev.XCampo.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rojas.dev.XCampo.dto.Notifications;
import com.rojas.dev.XCampo.entity.DeliveryProduct;
import com.rojas.dev.XCampo.entity.Order;
import com.rojas.dev.XCampo.entity.Product;
import com.rojas.dev.XCampo.enumClass.UserRole;
import com.rojas.dev.XCampo.event.PersistCreatedEvent;
import jakarta.persistence.PostPersist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEntityListener {

    private static ApplicationEventPublisher eventPublisher;

    @Autowired
    public void setEventPublisher(ApplicationEventPublisher eventPublisher) {
        OrderEntityListener.eventPublisher = eventPublisher;
    }

    @PostPersist
    public void onOrderCreated(Order order) {
        System.out.println("📩 Disparando evento de notificación para orden...");

        Notifications notification = new Notifications(UserRole.SELLER, "Nueva orden por atender ", order.getId_order().toString(), null,null);
        eventPublisher.publishEvent(new PersistCreatedEvent(notification));
    }
}
