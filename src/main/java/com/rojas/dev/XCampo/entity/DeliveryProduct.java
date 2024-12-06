package com.rojas.dev.XCampo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rojas.dev.XCampo.listeners.DeliveryEntityListener;
import com.rojas.dev.XCampo.enumClass.DeliveryProductState;
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
    private Long ID;

    @Temporal(TemporalType.DATE)
    private LocalDate date;

    private Boolean available;

    @Enumerated(EnumType.STRING)
    private DeliveryProductState state;

    private String startingPoint;
    
    private String destiny;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    private DeliveryMan deliveryMan;
}
