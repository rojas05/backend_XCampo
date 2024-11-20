package com.rojas.dev.XCampo.entity;

import com.rojas.dev.XCampo.enumClass.DeliveryProductState;
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

    private Boolean available;

    @Enumerated(EnumType.STRING)
    private DeliveryProductState deliveryProductState;

    private String startingPoint;
    
    private String destiny;

    @ManyToOne(fetch = FetchType.EAGER)
    private Order orderProducts;

    @OneToOne
    @JoinColumn(name = "fk_deliveryMan_id")
    private DeliveryMan deliveryMan;

}
