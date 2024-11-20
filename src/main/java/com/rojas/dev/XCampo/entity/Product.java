package com.rojas.dev.XCampo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rojas.dev.XCampo.enumClass.MeasurementUnit;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

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

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ItemCart> itemCarts;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Qualification> qualification;

    @ManyToOne(fetch = FetchType.EAGER)
    private Seller seller;

    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;

}
