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
    private UserRole nameRole;

    /*
    @OneToOne
    @JoinColumn(name = "roles_id", updatable = false, nullable = false)
    private Roles roles;


    @OneToOne(mappedBy = "rol", cascade = CascadeType.ALL)
    private Client client;


    @OneToOne(mappedBy = "rol", cascade = CascadeType.ALL)
    private DeliveryMan deliveryMan;
     */

    @ManyToOne
    @JoinColumn(name = "fk_user_id", updatable = false, nullable = false)
    private User user;

    @OneToOne(mappedBy = "rol", cascade = CascadeType.ALL)
    private Seller seller;

    @OneToOne(mappedBy = "rol", cascade = CascadeType.ALL)
    private Client client;

    @OneToOne(mappedBy = "rol", cascade = CascadeType.ALL)
    private DeliveryMan deliveryMan;
}
