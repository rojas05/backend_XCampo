package com.rojas.dev.XCampo.entityLocation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Municipio {
    private String id;
    private String nombre;
    private String departamentoId; // ID del país al que pertenece
}

