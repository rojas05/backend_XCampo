package com.rojas.dev.XCampo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "deliveryMan")
public class DeliveryMan {
    @Id
    private Long id_deliveryMan;

    private String rute;

    @OneToOne
    @JoinColumn(name = "user_id_user", updatable = false, nullable = false)
    private User user;

    @OneToMany(mappedBy = "deliveryMan", cascade = CascadeType.ALL)
    private Set<Order> orders = new HashSet<>();


}
