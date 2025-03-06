package com.rojas.dev.XCampo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetDeliveryPdtForDlvManDTO {
    private Long idDelivery;
    private String userName;
    private String startPointSeller;
    private String destinyClient;
    private Long idOrder;
    private Long idShoppingCard;
    private Set<CartItemDTO> products= new HashSet<>();
    private int deliveryCost;

    public GetDeliveryPdtForDlvManDTO(
            Long idDelivery,
            String userName,
            String startPointSeller,
            String destiny,
            Long idOrder,
            Long idShoppingCard
    ) {
        this.idDelivery = idDelivery;
        this.userName = userName;
        this.startPointSeller = startPointSeller;
        this.destinyClient = destiny;
        this.idOrder = idOrder;
        this.idShoppingCard = idShoppingCard;
    }
}
