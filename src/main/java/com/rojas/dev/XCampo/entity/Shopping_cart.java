package com.rojas.dev.XCampo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "shoppingCart")
public class Shopping_cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_cart;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Client client;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<CartItem> items = new HashSet<>();

    @Column(nullable = false)
    private boolean status = false;

    // @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private LocalDate dateAdded;

    @Column(nullable = false)
    private Double totalEarnings;

    @OneToOne(mappedBy = "shoppingCart")
    private Order order;
}
