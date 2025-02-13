package com.rojas.dev.XCampo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_cart_item;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_id_cart", nullable = false)
    private Shopping_cart cart;

    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;

    @Column(nullable = false)
    private Long quantity;

    @Column(nullable = false)
    private Long unitPrice;
}
