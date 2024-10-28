package com.rojas.dev.XCampo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "deliveryMan")
public class DeliveryMan {
    @Id
    private Long id_deliveryMan;

    private String rute;

    @OneToMany(mappedBy = "deliveryMan")
    private List<DeliveryProduct> deliveryProductsList;

    @OneToOne
    @JoinColumn(name = "rol_id", updatable = false, nullable = false)
    private Roles rol;

}
