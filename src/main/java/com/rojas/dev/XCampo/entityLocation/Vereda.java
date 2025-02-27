package com.rojas.dev.XCampo.entityLocation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vereda {
    private String id;
    private String nombre;
    private String municipioId; // ID del municipio al que pertenece
}

