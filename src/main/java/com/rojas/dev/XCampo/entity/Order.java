package com.rojas.dev.XCampo.entity;

import com.rojas.dev.XCampo.enumClass.OrderState;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Entity
@Table(name = "orderProducts")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_order;

    @Temporal(TemporalType.DATE)
    private LocalDate date;

    @Temporal(TemporalType.TIME)
    private LocalTime hour;

    @Enumerated(EnumType.STRING)
    private OrderState state;

    private String message;

    private Long price_delivery;

    private Boolean delivery;

    @OneToMany(mappedBy = "orderProducts")
    private List<DeliveryProduct> deliveryProductList;

    @OneToOne
    @JoinColumn(name = "fk_shoppingCart_id", updatable = false, nullable = false)
    private Shopping_cart shoppingCart;

    /* Que hace?
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<DeliveryProduct> orders = new HashSet<>();*/
}
