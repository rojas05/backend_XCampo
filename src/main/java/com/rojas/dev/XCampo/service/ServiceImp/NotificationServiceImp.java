package com.rojas.dev.XCampo.service.ServiceImp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rojas.dev.XCampo.dto.Notifications;
import com.rojas.dev.XCampo.enumClass.UserRole;
import com.rojas.dev.XCampo.repository.NotificationService;
import com.rojas.dev.XCampo.service.Interface.MatchmakingService;
import com.rojas.dev.XCampo.service.Interface.OrderService;
import com.rojas.dev.XCampo.service.Interface.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Service
public class NotificationServiceImp implements NotificationService {

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    @Autowired
    MatchmakingService matchmakingService;

    private final Queue<Notifications> pendingNotifications = new LinkedList<>();

    @Transactional
    @Override
    public void sendNotification(Notifications notifications ){
        if(notifications.getRole().equals(UserRole.CLIENT)){
            sendNotificationClient(notifications);
        } else if (notifications.getRole().equals(UserRole.SELLER)){
            sendNotificationSeller(notifications);
        } else if (notifications.getRole().equals(UserRole.DELIVERYMAN)){
            sendNotificationDelivery(notifications);
        }
    }

    private void sendNotificationDelivery(Notifications notifications) {
        try {
            System.out.println("📩 Procesando notificación para el rol: " + notifications.getRole());
            var fcmTokens = matchmakingService.match(notifications.getId());
            if ( fcmTokens == null || fcmTokens.isEmpty()) {
                System.out.println("❌ No hay usuarios con el rol " + notifications.getRole());
                return;
            }
            /*
             * Para obtener el primer valor se puede usar estos dos valores:
             * peek() retorna el primer valor sin eliminar
             * poll() retorna el primer valor y lo elimina
             */
            System.out.println("📌 Obteniendo lista: " + fcmTokens);
            addNotification(notifications);
        } catch (Exception e) {
            System.err.println("ERROR NOTIFICATION ====>" + e);
        }
    }

    void sendNotificationClient(Notifications notifications){
        try{
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

    void sendNotificationSeller(Notifications notifications){
        try{
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
        while (!pendingNotifications.isEmpty()) {
            Notifications notification = pendingNotifications.poll();
            if (notification != null) {
                try {
                    sendToFirebase(notification);
                } catch (Exception e) {
                    System.err.println("❌ Error al enviar notificación: " + e.getMessage());
                    pendingNotifications.offer(notification);
                }
            }
        }
    }

    void addNotification(Notifications notifications) {
        pendingNotifications.offer(notifications);
    }

    // Logic para mandar la notification cada cierto tiempo
    private void sendToFirebase(Notifications notifications) {
        System.out.println("🚀 Enviando notificación: " + notifications);
    }

}
