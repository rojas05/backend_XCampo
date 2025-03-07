package com.rojas.dev.XCampo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TokenNotificationID {

    private List<Long> Delivery;
    private String token;

    @Override
    public String toString() {
        return "{IdDelivery=" + Delivery + ", token='" + token + "'}";
    }
}
