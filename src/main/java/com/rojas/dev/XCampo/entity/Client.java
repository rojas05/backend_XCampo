package com.rojas.dev.XCampo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_client;

    private String name;

    private String location_description;

    @OneToOne
    @JoinColumn(name = "fk_rol_id", updatable = false, nullable = false)
    private Roles rol;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private Set<Shopping_cart> shoppingCart = new HashSet<>();

}
