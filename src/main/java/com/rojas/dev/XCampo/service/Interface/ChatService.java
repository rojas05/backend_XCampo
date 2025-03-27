package com.rojas.dev.XCampo.service.Interface;

import com.rojas.dev.XCampo.dto.ChatMessageDTO;

import java.util.List;

public interface ChatService {

    void sendMessage(ChatMessageDTO chatMessageDTO);

    List<ChatMessageDTO> getUserChat(String idOrder, String userId);

}
