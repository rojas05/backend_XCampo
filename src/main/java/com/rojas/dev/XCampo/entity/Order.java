package com.rojas.dev.XCampo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "order_products")
public class Order {
    @Id
    private Long id_order;

    private String state;

    private String message;

    private Long price_delivery;

    private Boolean delivery;

    @Temporal(TemporalType.DATE)
    private LocalDate date;

    @Temporal(TemporalType.TIME)
    private LocalTime hour;

    @ManyToOne(fetch = FetchType.EAGER)
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    private Seller seller;

    @OneToOne
    @JoinColumn(name = "shopping_cart_id_shopping_cart", updatable = false, nullable = false)
    private Shopping_cart shopping_cart;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<DeliveryProduct> orders = new HashSet<>();
}
