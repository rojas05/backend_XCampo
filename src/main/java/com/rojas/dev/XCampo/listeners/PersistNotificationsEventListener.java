package com.rojas.dev.XCampo.listeners;

import com.rojas.dev.XCampo.event.PersistCreatedEvent;
import com.rojas.dev.XCampo.repository.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class PersistNotificationsEventListener {
    private final NotificationService notificationService;

    @Autowired
    public PersistNotificationsEventListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @EventListener
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProductCreatedEvent(PersistCreatedEvent event) {
        System.out.println("📩 Procesando notificación en un nuevo contexto transaccional...");
        notificationService.sendNotification(event.getNotification());
    }
}
