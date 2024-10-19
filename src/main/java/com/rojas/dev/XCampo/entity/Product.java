package com.rojas.dev.XCampo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "product")
public class Product {
    @Id
    private Long id_product;

    private String name;

    private String description;

    private Integer stock;

    private String state;
}
