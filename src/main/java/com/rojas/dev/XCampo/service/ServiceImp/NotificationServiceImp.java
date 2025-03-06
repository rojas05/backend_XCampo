package com.rojas.dev.XCampo.service.ServiceImp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rojas.dev.XCampo.dto.Notifications;
import com.rojas.dev.XCampo.dto.TokenNotificationID;
import com.rojas.dev.XCampo.repository.DeliveryRepository;
import com.rojas.dev.XCampo.repository.NotificationService;
import com.rojas.dev.XCampo.service.Interface.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;

@Service
public class NotificationServiceImp implements NotificationService {

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    @Autowired
    DeliveryService deliveryService;

    @Autowired
    MatchmakingService matchmakingService;

    @Autowired
    DeliveryRepository deliveryRepository;

    @Autowired
    TaskService taskService;

    private final Queue<Notifications> pendingNotifications = new LinkedList<>();

    @Transactional
    @Override
    public void sendNotification(Notifications notifications) {
        switch (notifications.getRole()) {
            case CLIENT -> sendNotificationClient(notifications);
            case SELLER -> sendNotificationSeller(notifications);
            case DELIVERYMAN -> sendNotificationDelivery(notifications);
        }
    }

    void sendNotificationDelivery(Notifications notifications) {
        try {
            System.out.println("📩 Procesando notificación para el rol: " + notifications.getRole());
            Queue<TokenNotificationID> fcmTokens = matchmakingService.match(notifications.getId());
            LocalDateTime now = LocalDateTime.now();

            if (fcmTokens == null || fcmTokens.isEmpty()) {
                System.out.println("❌ No hay usuarios con el rol " + notifications.getRole());
                taskService.scheduleTask( now, () -> deliveryService.updateStateDeliverYMatch(notifications.getId()));
                return;
            }
            /*
             * Para obtener el primer valor se puede usar estos dos valores:
             * peek() retorna el primer valor sin eliminar
             * poll() retorna el primer valor y lo elimina
             */
            System.out.println("📌 Obteniendo lista de delivery: " + fcmTokens);

            List<Notifications> notificationList = fcmTokens.stream()
                    .map(tokenObj -> new Notifications(
                            notifications.getRole(),
                            notifications.getTitle(),
                            notifications.getMessage(),
                            Collections.singletonList(tokenObj.getToken()),
                            tokenObj.getIdDelivery()
                    ))
                    .toList();

            pendingNotifications.addAll(notificationList);
        } catch (Exception e) {
            System.err.println("ERROR NOTIFICATION ====>" + e);
        }
    }

    void sendNotificationClient(Notifications notifications) {
        try {
            System.out.println("📩 Procesando notificación para el rol: " + notifications.getRole());
            List<String> fcmTokens = userService.findFcmTokensByRole(notifications.getRole());
            if (fcmTokens.isEmpty()) {
                System.out.println("❌ No hay usuarios con el rol " + notifications.getRole());
                return;
            }
            createNotification(fcmTokens, notifications, false);

            /* Dividir tokens en lotes de 500
            final int batchSize = 500;
            for (int i = 0; i < fcmTokens.size(); i += batchSize) {
                List<String> batchTokens = fcmTokens.subList(i, Math.min(i + batchSize, fcmTokens.size()));

                Notifications notification = new Notifications(
                        notifications.getRole(),
                        notifications.getTitle(),
                        notifications.getMessage(),
                        batchTokens,
                        null
                );
                // Enviar el evento a Kafka
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                String notificationJson = mapper.writeValueAsString(notification);
                kafkaTemplate.send("product-notifications", notificationJson);
                System.out.println("evento enviado a Kafka");
            }*/
        } catch (Exception e) {
            System.err.println("ERROR NOTIFICATION CLIENTS ====>" + e);
        }
    }

    void sendNotificationSeller(Notifications notifications) {
        try {
            System.out.println("📩 Procesando notificación para el rol: " + notifications.getRole());
            List<String> fcmTokens = orderService.getNfsSellersByOrderId(notifications.getId());
            if (fcmTokens.isEmpty()) {
                System.out.println("❌ No hay usuarios con el rol " + notifications.getRole());
                System.out.println(fcmTokens);
                return;
            }
            createNotification(fcmTokens, notifications, true);

            /* Dividir tokens en lotes de 500
            final int batchSize = 500;
            for (int i = 0; i < fcmTokens.size(); i += batchSize) {
                List<String> batchTokens = fcmTokens.subList(i, Math.min(i + batchSize, fcmTokens.size()));

                Notifications notification = new Notifications(
                        notifications.getRole(),
                        notifications.getTitle(),
                        notifications.getMessage(),
                        batchTokens,
                        notifications.getId()
                );
                // Enviar el evento a Kafka
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                String notificationJson = mapper.writeValueAsString(notification);
                kafkaTemplate.send("product-notifications", notificationJson);
                System.out.println("evento enviado a Kafka");
            }*/
        } catch (Exception e) {
            System.err.println("ERROR NOTIFICATION SELLER ====>" + e);
        }
    }

    void createNotification(List<String> fcmTokens, Notifications notifications, boolean isID) {
        try {
            // Dividir tokens en lotes de 500
            final int batchSize = 500;
            for (int i = 0; i < fcmTokens.size(); i += batchSize) {
                List<String> batchTokens = fcmTokens.subList(i, Math.min(i + batchSize, fcmTokens.size()));

                Notifications notification = new Notifications(
                        notifications.getRole(),
                        notifications.getTitle(),
                        notifications.getMessage(),
                        batchTokens,
                        isID ? notifications.getId() : null
                );

                // Enviar el evento a Kafka
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                String notificationJson = mapper.writeValueAsString(notification);
                kafkaTemplate.send("product-notifications", notificationJson);

                System.out.println("Evento enviado a Kafka");
            }
        } catch (Exception e) {
            System.err.println("ERROR CREATE NOTIFICATION ====>" + e);
        }
    }

    @Scheduled(fixedRate = 900000) // 15 minutos
    void processPendingNotifications() {
        System.out.println("🔄 Procesando notificaciones pendientes...");

        while (!pendingNotifications.isEmpty()) {
            Notifications notification = pendingNotifications.poll();
            if (notification != null) {

                boolean isNotTake = deliveryRepository.verificateStateById(notification.getId());
                if (isNotTake) {
                    sendNotificationToKafka(notification)
                            .exceptionally(e -> {
                                System.err.println("❌ Error al enviar a Kafka: " + e.getMessage());
                                pendingNotifications.offer(notification);
                                return null;
                            });
                }
            }
        }
    }

    @Async("taskExecutor")
    CompletableFuture<Void> sendNotificationToKafka(Notifications notifications) {
        return CompletableFuture.runAsync(() -> {
            try {
                System.out.println("🚀 Enviando notificación: " + notifications);

                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                String notificationJson = mapper.writeValueAsString(notifications);
                kafkaTemplate.send("delivery-notifications", notificationJson);
            } catch (Exception e) {
                System.err.println("❌ Error al enviar notificación: " + e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }

}
