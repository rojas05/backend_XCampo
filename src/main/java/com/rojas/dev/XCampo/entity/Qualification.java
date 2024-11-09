package com.rojas.dev.XCampo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Calificacion")
public class Qualification {

    @Id
    private Long idQualification;
    private QualificationType qualificationType;
    private String description;

}
