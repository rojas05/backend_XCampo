package com.rojas.dev.XCampo.listeners;

import com.rojas.dev.XCampo.dto.Notifications;
import com.rojas.dev.XCampo.entity.Product;
import com.rojas.dev.XCampo.enumClass.UserRole;
import com.rojas.dev.XCampo.event.PersistCreatedEvent;
import jakarta.persistence.PostPersist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class ProductEntityListener {
    private static ApplicationEventPublisher eventPublisher;

    @Autowired
    public void setEventPublisher(ApplicationEventPublisher eventPublisher) {
        ProductEntityListener.eventPublisher = eventPublisher;
    }

    /**
     * evento de persistencia para producto
     * @param product
     */
    @PostPersist
    public void onProductCreated(Product product) {
        System.out.println("📩 Disparando evento de notificación para producto...");
        Notifications notification = new Notifications(
                UserRole.CLIENT,
                "Nuevo producto",
                product.getName(),
                null,
                product.getId_product(),
                "DetailProduct");

        eventPublisher.publishEvent(new PersistCreatedEvent(notification));
    }
}



