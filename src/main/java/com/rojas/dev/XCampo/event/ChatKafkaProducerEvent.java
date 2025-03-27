package com.rojas.dev.XCampo.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rojas.dev.XCampo.dto.ChatMessageDTO;
import com.rojas.dev.XCampo.dto.KafkaObjectChat;
import com.rojas.dev.XCampo.dto.Notifications;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ChatKafkaProducerEvent {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public ChatKafkaProducerEvent(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendMessage(String topic, ChatMessageDTO message, Notifications notifications) {
        try {
            KafkaObjectChat objects = new KafkaObjectChat(notifications, message);

            String notificationJson = objectMapper.writeValueAsString(objects);
            kafkaTemplate.send(topic, notificationJson);

            System.out.println("📤 Mensaje enviado a Kafka Topic [" + topic + "]: " + message);
        } catch (JsonProcessingException e) {
            System.err.println("❌ Error al enviar a Kafka: " + notifications.getRole() +
                    " Error:" + e.getMessage() + e);
        }
    }
}
