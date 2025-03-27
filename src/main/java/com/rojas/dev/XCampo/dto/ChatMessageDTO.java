package com.rojas.dev.XCampo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDTO {
    private String idChat;
    private String idOrder;
    private String senderId;
    private String receiverId;
    private String messageText;
    private Date timestamp;

    public String[] getUserTypeAndId(String userId) {
        if (userId == null || !userId.contains("_")) {
            throw new IllegalArgumentException("Formato inválido en el userId: " + userId);
        }

        return userId.split("_");
    }

    public Long getNumberIdOrder(String idOrder) {
        if (idOrder == null || !idOrder.contains("-")) {
            throw new IllegalArgumentException("Formato de ID de orden inválido: " + idOrder);
        }

        try {
            var id = idOrder.split("-");
            return Long.parseLong(id[1]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("No se pudo extraer el número de la orden: " + idOrder, e);
        }
    }
}
