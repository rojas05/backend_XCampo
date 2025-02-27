package com.rojas.dev.XCampo.event;

import com.rojas.dev.XCampo.dto.Notifications;

public class PersistCreatedEvent {
    private final Notifications notification;

    public PersistCreatedEvent(Notifications notification) {
        this.notification = notification;
    }

    public Notifications getNotification() {
        return notification;
    }
}

