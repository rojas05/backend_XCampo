package com.rojas.dev.XCampo.entity;

import com.rojas.dev.XCampo.enumClass.DeliveryProductState;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table (name = "Delivery")
public class DeliveryProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ID;

    @Temporal(TemporalType.DATE)
    private LocalDate date;

    private Boolean available;

    @Enumerated(EnumType.STRING)
    private DeliveryProductState state;

    private String startingPoint;
    
    private String destiny;

    @ManyToOne(fetch = FetchType.EAGER)
    private Order order;


    @ManyToOne(fetch = FetchType.EAGER)
    private DeliveryMan deliveryMan;

}
