package com.rojas.dev.XCampo.service.ServiceImp;

import com.rojas.dev.XCampo.dto.Notifications;
import com.rojas.dev.XCampo.dto.NotificationsDeliveryDto;
import com.rojas.dev.XCampo.dto.TokenNotificationID;
import com.rojas.dev.XCampo.service.Interface.NotificationService;
import com.rojas.dev.XCampo.service.Interface.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;

@Service
public class NotificationServiceImp implements NotificationService {

    private final UserService userService;
    private final OrderService orderService;
    private final DeliveryService deliveryService;
    private final MatchmakingService matchmakingService;
    private final TaskService taskService;
    private final FirebaseNotificationService firebaseNotificationService;
    public static final String CHAT_SCREEN = "ChatScreen";

    @Autowired
    public NotificationServiceImp(UserService userService, OrderService orderService, DeliveryService deliveryService, MatchmakingService matchmakingService, TaskService taskService, FirebaseNotificationService firebaseNotificationService) {
        this.userService = userService;
        this.orderService = orderService;
        this.deliveryService = deliveryService;
        this.matchmakingService = matchmakingService;
        this.taskService = taskService;
        this.firebaseNotificationService = firebaseNotificationService;
    }

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
                System.out.println("⚠ No hay usuarios para match con el rol por estar vació: " + notifications.getRole());
                taskService.scheduleTask(now, () -> deliveryService.updateStateDeliverYMatch(notifications.getId()));
                return;
            }

            System.out.println("📌 Obteniendo lista de delivery: " + fcmTokens);

            taskService.scheduleTasksSequentially(
                    fcmTokens,
                    () -> deliveryService.updateStateDeliverYMatch(notifications.getId()),
                    token -> {
                try {
                    System.out.println("🔄 Intentando enviar notificación a: " + token.getToken());

                    NotificationsDeliveryDto notificationToFirebase = new NotificationsDeliveryDto(
                            notifications.getRole(),
                            notifications.getTitle(),
                            notifications.getMessage(),
                            Collections.singletonList(token.getToken()),
                            token.getDelivery(),
                            "MapScreen"
                    );

                    sendNotificationToFirebase(notificationToFirebase)
                            .thenRun(() -> System.out.println("🚀 Notificación enviada con éxito de la tarea: " + token.getToken()))
                            .join();

                } catch (Exception e) {
                    System.err.println("❌ Error al enviar a Kafka: " + e.getMessage());
                }
            });
        } catch (Exception e) {
            System.err.println("ERROR NOTIFICATION ====>" + e);
        }
    }

    void sendNotificationClient(Notifications notifications) {
        try {
            System.out.println("📩 Procesando notificación para el rol: " + notifications.getRole());

            if (notifications.getScreen().equals(CHAT_SCREEN)) {
                var fcmToken = userService.findFcmTokensByIdClient(notifications.getId()); // Para enviar una sola notification
                if (fcmToken == null || fcmToken.isEmpty()) {
                    System.out.println("❌ No hay token FCM para el cliente.");
                    return;
                }

                createNotification(fcmToken, notifications, false);
            } else {
                List<String> fcmTokens = userService.findFcmTokensByRole(notifications.getRole());
                if (fcmTokens.isEmpty()) {
                    System.out.println("❌ No hay usuarios con el rol " + notifications.getRole());
                    return;
                }

                createNotifications(fcmTokens, notifications, false);
            }

        } catch (Exception e) {
            System.err.println("ERROR NOTIFICATION CLIENTS ====>" + e);
        }
    }

    void sendNotificationSeller(Notifications notifications) {
        try {
            System.out.println("📩 Procesando notificación para el rol: " + notifications.getRole());

            // Se podría optimizar para no repetir código lo está dentro del if
            if (notifications.getScreen().equals(CHAT_SCREEN)) {
                var fcmToken = userService.findFcmTokensByIdSeller(notifications.getId()); // Para enviar una sola notification
                if (fcmToken == null || fcmToken.isEmpty()) {
                    System.out.println("❌ No hay token FCM para el vendedor.");
                    return;
                }

                createNotification(fcmToken, notifications, true);
            } else {
                List<String> fcmTokens = orderService.getNfsSellersByOrderId(notifications.getId());

                if (fcmTokens.isEmpty()) {
                    System.out.println("❌ No hay usuarios con el rol " + notifications.getRole());
                    System.out.println(fcmTokens);
                    return;
                }

                createNotifications(fcmTokens, notifications, true);
            }

        } catch (Exception e) {
            System.err.println("ERROR NOTIFICATION SELLER ====>" + e);
        }
    }

    void createNotifications(List<String> fcmTokens, Notifications notifications, boolean isID) {
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
                        isID ? notifications.getId() : null,
                        notifications.getScreen()
                );

                /* Enviar el evento a Kafka
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                String notificationJson = mapper.writeValueAsString(notification);
                kafkaTemplate.send("product-notifications", notificationJson);*/
                firebaseNotificationService.sendNotifications(notification);

                System.out.println("Evento enviado a Firebase");
            }
        } catch (Exception e) {
            System.err.println("ERROR CREATE NOTIFICATION ====>" + e);
        }
    }

    void createNotification(String fcmToken, Notifications notifications, boolean isID) {
        try {
                Notifications notification = new Notifications(
                        notifications.getRole(),
                        notifications.getTitle(),
                        notifications.getMessage(),
                        Collections.singletonList(fcmToken),
                        isID ? notifications.getId() : null,
                        notifications.getScreen()
                );

                firebaseNotificationService.sendNotifications(notification);

                System.out.println("Evento enviado a Firebase");

        } catch (Exception e) {
            System.err.println("ERROR CREATE NOTIFICATION ====>" + e);
        }
    }

    @Async("taskExecutor")
    CompletableFuture<Void> sendNotificationToFirebase(NotificationsDeliveryDto notifications) {
        return CompletableFuture.runAsync(() -> {
            try {
                System.out.println("🚀 Enviando notificación a Firebase: " + notifications);
                firebaseNotificationService.sendNotification(notifications);
            } catch (Exception e) {
                System.err.println("[TaskExecutor] ❌ Error al enviar notificación: " + e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }

}
