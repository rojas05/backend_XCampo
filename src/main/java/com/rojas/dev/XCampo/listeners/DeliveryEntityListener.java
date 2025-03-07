package com.rojas.dev.XCampo.listeners;

import com.rojas.dev.XCampo.dto.Notifications;
import com.rojas.dev.XCampo.entity.DeliveryProduct;
import com.rojas.dev.XCampo.enumClass.UserRole;
import com.rojas.dev.XCampo.event.PersistCreatedEvent;
import jakarta.persistence.PostPersist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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
        Notifications notification = new Notifications(UserRole.DELIVERYMAN, "Nuevo delivery", delivery.getDate().toString(), null, delivery.getId(),"delivery");
        eventPublisher.publishEvent(new PersistCreatedEvent(notification));
    }


}
