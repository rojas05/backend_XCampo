package com.rojas.dev.XCampo.service.Interface;

import com.rojas.dev.XCampo.dto.TokenNotificationID;

import java.util.Queue;

public interface MatchmakingService {
    Queue<TokenNotificationID> match(Long id);
}
