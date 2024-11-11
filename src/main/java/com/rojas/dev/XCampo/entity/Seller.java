package com.rojas.dev.XCampo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "seller")
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_seller;

    private String name_store;

    @Column(columnDefinition = "POINT")
    private Point coordinates;

    private String location;

    private String location_description;

    private String img;

    @OneToOne
    @JoinColumn(name = "fk_rol_id", updatable = false, nullable = false)
    private Roles rol;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private List<Product> productList;
}
