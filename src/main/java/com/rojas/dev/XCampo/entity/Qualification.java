package com.rojas.dev.XCampo.entity;

import com.rojas.dev.XCampo.enumClass.QualificationType;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Qualification")
public class Qualification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idQualification;

    @Enumerated(EnumType.STRING)
    private QualificationType qualificationType;

    @Column(length = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;

}
