package com.rojas.dev.XCampo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rojas.dev.XCampo.enumClass.MeasurementUnit;
import com.rojas.dev.XCampo.listeners.ProductEntityListener;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "products")
@EntityListeners(ProductEntityListener.class)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_product;

    //@NotNull(message = "El nombre no puede ser nulo")
    private String name;

    private String description;

    private Long stock;

    private Boolean state;

    private Integer price;

    @Enumerated(EnumType.STRING)
    private MeasurementUnit measurementUnit;

    @Column(length = 1000)
    private String urlImage;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    private Set<CartItem> itemCarts = new HashSet<>();


    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<Qualification> qualification = new HashSet<>();


    @ManyToOne(fetch = FetchType.EAGER)
    private Seller seller;

    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;

}
