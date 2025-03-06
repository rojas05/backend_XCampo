package com.rojas.dev.XCampo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rojas.dev.XCampo.enumClass.DeliveryProductState;
import com.rojas.dev.XCampo.listeners.DeliveryEntityListener;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table (name = "Delivery")
@EntityListeners(DeliveryEntityListener.class)
public class DeliveryProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private LocalDate date;

    private Boolean available;

    @Enumerated(EnumType.STRING)
    private DeliveryProductState state;

    private String startingPoint;
    
    private String destiny;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    private DeliveryMan deliveryMan;
}
