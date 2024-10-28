package com.rojas.dev.XCampo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "seller")
public class Seller {

    @Id
    private Long id_seller;

    private String name_store;

    @Column(columnDefinition = "POINT")
    private Point coordinates;

    private String location;

    private String location_description;

    private String img;

    /*
    @OneToOne(mappedBy = "rol", cascade = CascadeType.ALL)
    private Seller seller;
     */

    @OneToOne
    @JoinColumn(name = "rol_id", updatable = false, nullable = false)
    private Roles rol;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private Set<Order> orders = new HashSet<>();
}
