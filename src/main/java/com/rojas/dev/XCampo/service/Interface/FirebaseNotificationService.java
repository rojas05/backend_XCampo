package com.rojas.dev.XCampo.service.Interface;

import com.rojas.dev.XCampo.dto.Notifications;
import com.rojas.dev.XCampo.dto.NotificationsDeliveryDto;

public interface FirebaseNotificationService {

    void sendNotifications(Notifications notifications);

    void sendNotification(NotificationsDeliveryDto notifications);
}
