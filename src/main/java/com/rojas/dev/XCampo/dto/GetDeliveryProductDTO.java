package com.rojas.dev.XCampo.dto;

import com.rojas.dev.XCampo.enumClass.DeliveryProductState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetDeliveryProductDTO {
    private Long idDelivery;
    private LocalDate date;
    private Boolean available;
    private DeliveryProductState state;
    private String startingPoint;
    private String destiny;
    private Long orderId;
    private Long deliveryManId;
}
