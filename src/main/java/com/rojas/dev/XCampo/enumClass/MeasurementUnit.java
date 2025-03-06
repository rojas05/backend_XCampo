package com.rojas.dev.XCampo.enumClass;

import java.util.Arrays;
import java.util.Optional;

public enum MeasurementUnit {
    KILOGRAMO,
    GRAMO,
    LIBRA,
    LITRO,
    ARROBA;

    public static Optional<MeasurementUnit> fromString(String value) {
        return Arrays.stream(com.rojas.dev.XCampo.enumClass.MeasurementUnit.values())
                .filter(state -> state.name().replace("_", " ")
                        .equalsIgnoreCase(value.replace("_", " ")))
                .findFirst();
    }
}
