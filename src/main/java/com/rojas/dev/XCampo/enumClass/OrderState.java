package com.rojas.dev.XCampo.enumClass;

import java.util.Arrays;
import java.util.Optional;

public enum OrderState {
    EN_ESPERA,
    ACEPTADA,
    CANCELADA,
    LISTA_ENVIAR,
    FINALIZADA;

    // Metodo para encontrar el estado correspondiente
    public static Optional<OrderState> fromString(String value) {
        return Arrays.stream(OrderState.values())
                .filter(state -> state.name().replace("_", " ")
                        .equalsIgnoreCase(value.replace("_", " ")))
                .findFirst();
    }
}
