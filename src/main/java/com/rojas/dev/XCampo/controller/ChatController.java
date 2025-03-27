package com.rojas.dev.XCampo.controller;

import com.rojas.dev.XCampo.dto.ChatMessageDTO;
import com.rojas.dev.XCampo.service.Interface.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody ChatMessageDTO chatMessageDTO) {
        chatService.sendMessage(chatMessageDTO);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Mensaje enviado correctamente");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/messages/{idOrder}/{userId}")
    public ResponseEntity<?> getMessages(
            @PathVariable String idOrder,
            @PathVariable String userId) {

        List<ChatMessageDTO> messages = chatService.getUserChat(idOrder, userId);
        return ResponseEntity.ok(messages);
    }

}
