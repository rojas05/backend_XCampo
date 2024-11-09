package com.rojas.dev.XCampo.entity;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

@Data
@Entity
@Table
public class Shopping_cart {
    @Id
    private Long id_shopping_cart;

    private Long amount;

    @OneToOne
    @JoinColumn(name = "shopping_cart", updatable = false, nullable = false)
    private Shopping_cart shoppingCart;

    // conectar producto
}
