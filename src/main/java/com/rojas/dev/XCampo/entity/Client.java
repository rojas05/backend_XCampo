package com.rojas.dev.XCampo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "client")
public class Client {
    @Id
    private Long id_client;

    private String name;

    private String location_description;

    @OneToOne
    @JoinColumn(name = "roles_id", updatable = false, nullable = false)
    private Roles roles;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private Set<Order> orders = new HashSet<>();
}
