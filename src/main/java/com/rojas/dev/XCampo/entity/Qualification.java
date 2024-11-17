package com.rojas.dev.XCampo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Calificacion")
public class Qualification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idQualification;

    @Enumerated(EnumType.STRING)
    private QualificationType qualificationType;

    private String description;

    @ManyToOne
    @JoinColumn(name = "fk_product_id")
    private Product products;

}
