package com.rojas.dev.XCampo.enumClass;

import java.util.Arrays;
import java.util.Optional;

/**
 * enum class co estado nuevo "EN_COLA"
 */
public enum DeliveryProductState {
    DISPONIBLE,
    EN_COLA,
    TOMADO,
    RECOGIDO,
    EN_CAMINO,
    ENTREGADO;

    public static Optional<DeliveryProductState> fromStringDeliveryState(String value) {
        return Arrays.stream(com.rojas.dev.XCampo.enumClass.DeliveryProductState.values())
                .filter(state -> state.name().replace("_", " ")
                        .equalsIgnoreCase(value.replace("_", " ")))
                .findFirst();
    }
}
