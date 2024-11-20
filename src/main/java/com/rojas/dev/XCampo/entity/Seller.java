package com.rojas.dev.XCampo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "seller")
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_seller;

    private String name_store;

    private String coordinates;

    private String location;

    private String location_description;

    private BigDecimal totalEarnings;

    @Column(length = 1000)
    private String img;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_rol_id")
    private Roles rol;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private List<Product> product;

}
