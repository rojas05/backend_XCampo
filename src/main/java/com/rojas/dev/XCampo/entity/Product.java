package com.rojas.dev.XCampo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "product")
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

    private String UrlImage;

    // conectar a carrito

    
}
