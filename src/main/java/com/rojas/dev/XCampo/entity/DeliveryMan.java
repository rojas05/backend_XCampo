package com.rojas.dev.XCampo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "deliveryMan")
public class DeliveryMan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_deliveryMan;

    private String rute;

    @OneToOne(mappedBy = "deliveryMan", cascade = CascadeType.ALL)
    private DeliveryProduct deliveryProducts;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_rol_id")
    private Roles rol;

}
