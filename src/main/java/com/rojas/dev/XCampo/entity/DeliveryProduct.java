package com.rojas.dev.XCampo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table (name = "EnvioProducto")
public class DeliveryProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ID;

    @Temporal(TemporalType.DATE)
    private LocalDate date;

    private String state;

    @ManyToOne
    @JoinColumn(name = "orderId")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "deliveryManId")
    private DeliveryMan deliveryMan;

}
