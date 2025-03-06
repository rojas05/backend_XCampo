package com.rojas.dev.XCampo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryGroupedBySellerDTO {
    private Long sellerId;
    private String sellerName;
    private String starPointSeller;
    private Long totalOrders;
    private List<GetDeliveryPdtForDlvManDTO> orders;

    public DeliveryGroupedBySellerDTO(Long sellerId, String sellerName, String starPointSeller, Long totalOrders) {
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.starPointSeller = starPointSeller;
        this.totalOrders = totalOrders;
    }
}
