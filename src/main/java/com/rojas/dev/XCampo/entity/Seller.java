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

    @OneToOne
    @JoinColumn(name = "roles_id", updatable = false, nullable = false)
    private Roles roles;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private Set<Order> orders = new HashSet<>();
}
