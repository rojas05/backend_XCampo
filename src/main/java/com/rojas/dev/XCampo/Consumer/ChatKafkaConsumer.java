package com.rojas.dev.XCampo.Consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rojas.dev.XCampo.dto.KafkaObjectChat;
import com.rojas.dev.XCampo.service.Interface.NotificationService;
import com.rojas.dev.XCampo.service.ServiceImp.FirebaseChatServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ChatKafkaConsumer {

    @Autowired
    FirebaseChatServiceImp firebaseChatServiceImp;

    @Autowired
    NotificationService notificationService;

    @Autowired
    ObjectMapper objectMapper;

    @KafkaListener(topics = "chat-messages", groupId = "chat-messages")
    public void consumerMessageEvent(String message) {
        try {
            System.out.println("📥 Mensaje recibido de Kafka: " + message);
            KafkaObjectChat notification  = objectMapper.readValue(message, KafkaObjectChat.class);

            firebaseChatServiceImp.storeMessageInFirebase(notification.getChatMessage());
            notificationService.sendNotification(notification.getChatsNotifications());
        } catch (JsonProcessingException e) {
            System.err.println("❌ Error al consumir la notificación par el chat: " + e);
        }
    }
}
