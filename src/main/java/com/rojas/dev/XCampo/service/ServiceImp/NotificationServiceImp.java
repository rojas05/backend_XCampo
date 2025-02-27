package com.rojas.dev.XCampo.service.ServiceImp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rojas.dev.XCampo.dto.Notifications;
import com.rojas.dev.XCampo.enumClass.UserRole;
import com.rojas.dev.XCampo.repository.NotificationService;
import com.rojas.dev.XCampo.repository.UserRepository;
import com.rojas.dev.XCampo.service.Interface.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImp implements NotificationService {

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    UserService userService;

    @Transactional
    @Override
    public void sendNotification(Notifications notifications ){
        if(notifications.getRole().equals(UserRole.CLIENT)){
            sendNotificationClient(notifications);
        } else if (notifications.getRole().equals(UserRole.SELLER)){
            sendNotificationSeller(notifications);
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
            // Dividir tokens en lotes de 500
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
            }
        } catch (Exception e) {
            System.err.println("ERROR ====>" + e);
        }
    }

    void sendNotificationSeller(Notifications notifications){
        try{
            System.out.println("📩 Procesando notificación para el rol: " + notifications.getRole());
            List<String> fcmTokens = userService.findFcmTokensByRole(notifications.getRole());
            if (fcmTokens.isEmpty()) {
                System.out.println("❌ No hay usuarios con el rol " + notifications.getRole());
                return;
            }
                Notifications notification = new Notifications(
                        notifications.getRole(),
                        notifications.getTitle(),
                        notifications.getMessage(),
                        notifications.getTokens(),
                        null
                );
                // Enviar el evento a Kafka
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                String notificationJson = mapper.writeValueAsString(notification);
                kafkaTemplate.send("order-notifications", notificationJson);
                System.out.println("evento enviado a Kafka");

        } catch (Exception e) {
            System.err.println("ERROR ====>" + e);
        }
    }

}
