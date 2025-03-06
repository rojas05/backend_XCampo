package com.rojas.dev.XCampo.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class TokenNotificationID {
    private Long IdDelivery;
    private String token;

    @Override
    public String toString() {
        return "{IdDelivery=" + IdDelivery + ", token='" + token + "'}";
    }
}
