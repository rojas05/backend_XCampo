package com.rojas.dev.XCampo.repository;

import com.rojas.dev.XCampo.dto.Notifications;


public interface NotificationService {
    void sendNotification(Notifications notification);
}
