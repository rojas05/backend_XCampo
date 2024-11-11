package com.rojas.dev.XCampo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table (name = "delivery")
public class DeliveryProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_delivery;

    @Temporal(TemporalType.DATE)
    private LocalDate date;

    private String state;

    @ManyToOne(fetch = FetchType.EAGER)
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    private DeliveryMan deliveryMan;

}
