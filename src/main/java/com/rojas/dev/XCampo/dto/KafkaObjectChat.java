package com.rojas.dev.XCampo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class KafkaObjectChat {
    Notifications chatsNotifications;
    ChatMessageDTO chatMessage;
}
