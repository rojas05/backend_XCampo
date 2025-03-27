package com.rojas.dev.XCampo.event;

import com.rojas.dev.XCampo.dto.Notifications;

/**
 * creacion de los eventos de las notificaciones.
 */
public class PersistCreatedEvent {
    private final Notifications notification;

    /**
     * creacion del evento
     * @param notification
     */
    public PersistCreatedEvent(Notifications notification) {
        this.notification = notification;
    }

    /**
     * Metodo de acceso al evento
     * @return
     */
    public Notifications getNotification() {
        return notification;
    }
}

