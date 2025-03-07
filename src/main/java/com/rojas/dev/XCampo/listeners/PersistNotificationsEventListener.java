package com.rojas.dev.XCampo.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rojas.dev.XCampo.dto.Notifications;
import com.rojas.dev.XCampo.event.PersistCreatedEvent;
import com.rojas.dev.XCampo.repository.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.concurrent.CompletableFuture;

@Component
public class PersistNotificationsEventListener {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public PersistNotificationsEventListener(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @EventListener
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProductCreatedEvent(PersistCreatedEvent event) {
        System.out.println("📩 Procesando notificación en un nuevo contexto transaccional...");

        switch ( event.getNotification().getRole() ) {
            case CLIENT, SELLER -> addKafkaEvent("product-notifications", event.getNotification());
            case DELIVERYMAN -> addKafkaEvent("delivery-notifications", event.getNotification());
        }
    }

    @Async("taskExecutor")
    CompletableFuture<Void> addKafkaEvent(String topic, Notifications notification) {
        return CompletableFuture.runAsync(() -> {
            try {
                System.out.println("🚀 Evento enviado a Kafka: " + notification);

                String notificationJson = objectMapper.writeValueAsString(notification);
                kafkaTemplate.send(topic, notificationJson);
            } catch (Exception e) {
                System.err.println("❌ Error al enviar a Kafka: " + notification.getRole() + " Error:" + e.getMessage() + e);
            }
        });
    }
}
