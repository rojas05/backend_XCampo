package com.rojas.dev.XCampo.entity;

import jakarta.persistence.*;
import lombok.Data;

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

    @ManyToOne(fetch = FetchType.EAGER)
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    private Seller seller;

    @ManyToOne(fetch = FetchType.EAGER)
    private DeliveryMan deliveryMan;

    @OneToOne
    @JoinColumn(name = "shopping_cart_id_shopping_cart", updatable = false, nullable = false)
    private Shopping_cart shopping_cart;

}
