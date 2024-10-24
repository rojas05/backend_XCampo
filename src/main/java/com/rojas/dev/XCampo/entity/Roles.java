package com.rojas.dev.XCampo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "role")
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roles_id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private UserRole nameRole;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    @OneToOne(mappedBy = "roles", cascade = CascadeType.ALL)
    private Client client;

    @OneToOne(mappedBy = "roles", cascade = CascadeType.ALL)
    private Seller seller;

    @OneToOne(mappedBy = "roles", cascade = CascadeType.ALL)
    private DeliveryMan deliveryMan;
}
