package com.rojas.dev.XCampo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

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

    @OneToOne
    @JoinColumn(name = "fk_rol_id", updatable = false, nullable = false)
    private Roles rol;

}
