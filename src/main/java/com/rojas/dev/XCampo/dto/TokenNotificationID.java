package com.rojas.dev.XCampo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TokenNotificationID {

    private Long IdDelivery;
    private String token;

    @Override
    public String toString() {
        return "{IdDelivery=" + IdDelivery + ", token='" + token + "'}";
    }
}
