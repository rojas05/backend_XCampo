package com.rojas.dev.XCampo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "cart")
public class Shopping_cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_cart;

    @ManyToOne(fetch = FetchType.LAZY)
    private Client client;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private Set<CartItem> items = new HashSet<>();

    @Column(nullable = false)
    private boolean status = false;

    // @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private LocalDate dateAdded;

    /**
     * Agrega un CartItem al carrito.
     * @param item El CartItem que se desea agregar.
     */
    public void addItem(CartItem item) {
        item.setCart(this);
        this.items.add(item);
    }

    /**
     * Elimina un CartItem del carrito.
     * @param item El CartItem que se desea eliminar.
     */
    public void removeItem(CartItem item) {
        this.items.remove(item);
        item.setCart(null); // Romper la relación bidireccional
    }
}
