package com.rojas.dev.XCampo.enumClass;

import java.util.Arrays;

public enum OrderState {
    EN_ESPERA,
    ACEPTADA,
    CANCELADA,
    LISTA_ENVIAR,
    FINALIZADA;

    public static boolean contains(String value) {
        return Arrays.stream(OrderState.values())
                .anyMatch(state -> state.name().equalsIgnoreCase(value));
    }
}
