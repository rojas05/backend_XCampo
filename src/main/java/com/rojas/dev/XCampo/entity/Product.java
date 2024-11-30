package com.rojas.dev.XCampo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rojas.dev.XCampo.enumClass.MeasurementUnit;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_product;

    @NotNull(message = "El nombre no puede ser nulo")
    private String name;

    private String description;

    private Integer stock;

    private Boolean state;

    private Double price;

    @Enumerated(EnumType.STRING)
    private MeasurementUnit measurementUnit;

    @Column(length = 1000)
    private String UrlImage;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    private Set<CartItem> itemCarts = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<Qualification> qualification = new HashSet<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private Seller seller;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;

}
