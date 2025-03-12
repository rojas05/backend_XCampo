package com.rojas.dev.XCampo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rojas.dev.XCampo.enumClass.OrderState;
import com.rojas.dev.XCampo.listeners.OrderEntityListener;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "orderProducts")
@EntityListeners(OrderEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_order;

    @Temporal(TemporalType.DATE)
    private LocalDate date;

    @Temporal(TemporalType.TIME)
    private LocalTime hour;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderState state;

    private String message;

    @Column(nullable = false)
    private Long price_delivery;

    private Boolean delivery;

    @JsonIgnore
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DeliveryProduct> deliveryProduct = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "fk_shoppingCart_id", updatable = false, nullable = false)
    private Shopping_cart shoppingCart;

}
