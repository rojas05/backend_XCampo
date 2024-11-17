package com.rojas.dev.XCampo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_product;

    @NotNull(message = "El nombre no puede ser nulo")
    private String name;

    private String description;

    private Integer stock;

    private Boolean state;

    private Double price;

    private MeasurementUnit measurementUnit;

    private List<String> UrlImage;

    @OneToMany(mappedBy = "products", cascade = CascadeType.ALL)
    private List<Shopping_cart> shoppingCartList;

    @OneToMany(mappedBy = "products", cascade = CascadeType.ALL)
    private List<Qualification> qualificationList;

    @ManyToOne
    @JoinColumn(name = "fk_seller_id")
    private Seller seller;

    @ManyToOne
    @JoinColumn(name = "fk_category_id")
    private Category category;

}
