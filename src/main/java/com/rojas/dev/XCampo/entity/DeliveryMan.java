package com.rojas.dev.XCampo.entity;

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

    @OneToMany(mappedBy = "deliveryMan")
    private Set<DeliveryProduct> deliveryProducts = new HashSet<>();

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_rol_id")
    private Roles rol;

}
