package com.rojas.dev.XCampo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.HashSet;
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

    private String locationDestiny;

    @OneToOne
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "fk_rol_id", updatable = false, nullable = false)
    private Roles rol;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Shopping_cart> shoppingCart = new HashSet<>();

}
