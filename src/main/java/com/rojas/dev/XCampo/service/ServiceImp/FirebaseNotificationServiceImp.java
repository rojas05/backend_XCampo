package com.rojas.dev.XCampo.service.ServiceImp;

import com.google.firebase.messaging.*;

import com.rojas.dev.XCampo.dto.Notifications;
import com.rojas.dev.XCampo.service.Interface.FirebaseNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FirebaseNotificationServiceImp implements FirebaseNotificationService {

    @Autowired
    private final FirebaseMessaging firebaseMessaging;

    public FirebaseNotificationServiceImp(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }

    @Override
    public void sendNotifications(Notifications notifications) {
        try {
            // Construir la notificación visible
            Notification notification = Notification.builder()
                    .setTitle(notifications.getTitle())
                    .setBody(notifications.getMessage())
                    .build();

            // Verificar si hay tokens disponibles
            List<String> tokens = notifications.getTokens();
            if (tokens == null || tokens.isEmpty()) {
                System.out.println("⚠ No hay tokens disponibles para enviar la notificación.");
                return;
            }

            System.out.println(verifyTokens(tokens));
            System.out.println(notification);

            // Agregar datos personalizados para la navegación en la app
            Map<String, String> data = new HashMap<>();
            data.put("screen", notifications.getScreen()); // Nombre de la pantalla
            data.put("orderId", String.valueOf(notifications.getId())); // Ejemplo de parámetro adicional

            // Crear mensaje multicast con datos adicionales
            MulticastMessage message = MulticastMessage.builder()
                    .addAllTokens(tokens)
                    .setNotification(notification) //Notificación visible
                    .putAllData(data) //Datos personalizados
                    .build();

            System.out.println(message);
            BatchResponse response = firebaseMessaging.sendEachForMulticast(message);
            System.out.println("✅ Notificación enviada: " + response.getSuccessCount() + " exitosas, " +
                    response.getFailureCount() + " fallidas.");
        } catch (FirebaseMessagingException e) {
            if (e.getMessagingErrorCode() == MessagingErrorCode.INVALID_ARGUMENT) {
                System.err.println("❌ Token inválido: ");
            } else {
                System.err.println("❌ Error al enviar notificación: " + e.getMessage());
            }
        }
    }

    @Override
    public void sendNotification(Notifications notifications) {
        try {
            Message message = Message.builder()
                    .setToken(notifications.getFirstToken())
                    .setNotification(Notification.builder()
                            .setTitle(notifications.getTitle())
                            .setBody(notifications.getMessage())
                            .build())
                    .build();

            System.err.println(message);
            String response = firebaseMessaging.send(message);
            System.out.println("Notificación enviada: " + response);
        } catch (FirebaseMessagingException e) {
            if (e.getMessagingErrorCode() == MessagingErrorCode.INVALID_ARGUMENT) {
                System.err.println("❌ Token inválido: ");
            } else {
                System.err.println("❌ Error al enviar notificación: " + e.getMessage());
            }
        }
    }

    List<String> verifyTokens(List<String> tokens){
        List<String> listCheck = new ArrayList<>();
        for (String token : tokens) {
            try {
                firebaseMessaging.send(Message.builder().setToken(token).build());
                listCheck.add(token);
            } catch (FirebaseMessagingException e) {
                System.err.println("❌ Token inválido: " + token);
            }
        }
        return listCheck;
    }
}

