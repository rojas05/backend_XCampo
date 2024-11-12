package com.rojas.dev.XCampo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "seller")
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_seller;

    private String name_store;

    private String coordinates;

    private String location;

    private String location_description;

    @Column(length = 1000)
    private String img;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rol_id")
    private Roles rol;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private Set<Order> orders = new HashSet<>();

}
