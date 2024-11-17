package com.rojas.dev.XCampo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "shoppingCart")
public class Shopping_cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_shopping_cart;

    private Long amount;

    /* Que hace? xq se llama haci misma
    @OneToOne
    @JoinColumn(name = "shopping_cart", updatable = false, nullable = false)
    private Shopping_cart shoppingCart; */

    @OneToOne(mappedBy = "shoppingCart", cascade = CascadeType.ALL)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "fk_client_id")
    private Client clients;

    @ManyToOne
    @JoinColumn(name = "fk_product_id")
    private Product products;
}
