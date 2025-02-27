package com.rojas.dev.XCampo.service.Interface;

import com.rojas.dev.XCampo.dto.Notifications;

public interface FirebaseNotificationService {

    void sendNotifications(Notifications notifications);

    void sendNotification(Notifications notifications);
}
