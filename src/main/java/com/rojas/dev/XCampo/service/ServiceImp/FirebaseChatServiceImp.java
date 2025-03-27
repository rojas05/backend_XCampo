package com.rojas.dev.XCampo.service.ServiceImp;

import com.google.firebase.database.*;
import com.rojas.dev.XCampo.dto.ChatMessageDTO;
import com.rojas.dev.XCampo.dto.Notifications;
import com.rojas.dev.XCampo.enumClass.UserRole;
import com.rojas.dev.XCampo.event.ChatKafkaProducerEvent;
import com.rojas.dev.XCampo.exception.InvalidDataException;
import com.rojas.dev.XCampo.service.Interface.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class FirebaseChatServiceImp implements ChatService {

    private final DatabaseReference chatDatabase;
    private final ChatKafkaProducerEvent chatKafkaProducerEvent;

    @Autowired
    public FirebaseChatServiceImp(FirebaseDatabase firebaseDatabase, ChatKafkaProducerEvent chatKafkaProducer) {
        this.chatDatabase = firebaseDatabase.getReference("chats");
        this.chatKafkaProducerEvent = chatKafkaProducer;
    }

    @Override
    public void sendMessage(ChatMessageDTO chatMessageDTO) {
        if (chatMessageDTO.getSenderId() == null || chatMessageDTO.getReceiverId() == null) {
            throw new InvalidDataException("El remitente y el destinatario no pueden ser nulos");
        }

        if (chatMessageDTO.getMessageText() == null || chatMessageDTO.getMessageText().trim().isEmpty()) {
            throw new InvalidDataException("El mensaje no puede estar vacío");
        }

        var receiver = chatMessageDTO.getReceiverId();
        String[] receiverInfo = chatMessageDTO.getUserTypeAndId(receiver);

        Notifications notifications = new Notifications(
                UserRole.fromString(receiverInfo[0]),
                "Tienes un nuevo mensaje",
                chatMessageDTO.getMessageText(),
                null,
                Long.parseLong(receiverInfo[1]),
                "ChatScreen"
        );

        // Publicar en Kafka
        chatKafkaProducerEvent.sendMessage("chat-messages", chatMessageDTO, notifications);
        System.out.println("📩 Mensaje enviado a Kafka: " + chatMessageDTO);
    }

    @Override
    public List<ChatMessageDTO> getUserChat(String idOrder, String userId) {
        CompletableFuture<List<ChatMessageDTO>> futureMessages = new CompletableFuture<>();

        chatDatabase.child(idOrder)
                .orderByChild("timestamp")
                .limitToLast(50)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<ChatMessageDTO> messages = new ArrayList<>();

                        for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                            ChatMessageDTO message = messageSnapshot.getValue(ChatMessageDTO.class);

                            // Mostrar solo los mensajes donde el usuario sea remitente o receptor
                            if (message != null &&
                                    (message.getSenderId().equals(userId) || message.getReceiverId().equals(userId))) {
                                message.setIdChat(messageSnapshot.getKey());
                                messages.add(message);
                            }
                        }

                        futureMessages.complete(messages);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        futureMessages.completeExceptionally(new RuntimeException(
                                "Error al recuperar mensajes: " + databaseError.getMessage()));
                    }
                });

        try {
            return futureMessages.get();
        } catch (Exception e) {
            throw new RuntimeException("Error al recuperar mensajes", e);
        }
    }

    public void storeMessageInFirebase(ChatMessageDTO chatMessageDTO) {
        String messageId = chatDatabase.push().getKey();
        if (messageId == null) {
            System.err.println("❌ Error: No se pudo generar un ID de mensaje en Firebase");
            return;
        }

        chatMessageDTO.setIdChat(messageId);
        Map<String, Object> messageData = new HashMap<>();
        messageData.put("senderId", chatMessageDTO.getSenderId());
        messageData.put("receiverId", chatMessageDTO.getReceiverId());
        messageData.put("messageText", chatMessageDTO.getMessageText());
        messageData.put("timestamp", chatMessageDTO.getTimestamp());

        chatDatabase.child(chatMessageDTO.getIdOrder()).child(chatMessageDTO.getIdChat()).setValueAsync(messageData);
        System.out.println("💾 Mensaje guardado en Firebase: " + chatMessageDTO);
    }

}
