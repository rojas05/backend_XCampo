package com.rojas.dev.XCampo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    private Long id_user;

    private String name;

    private String city;

    private Long cell;

    private String email;

    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Client client;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Seller seller;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private DeliveryMan deliveryMan;

}
