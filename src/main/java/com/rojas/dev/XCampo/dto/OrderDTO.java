package com.rojas.dev.XCampo.dto;

import com.rojas.dev.XCampo.enumClass.OrderState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long idOrder;
    private OrderState state;
    private String message;
    private Boolean delivery;
    private Long priceDelivery;
    private GetShoppingCartDTO shoppingCartId;
    //private List<GetDeliveryProductDTO> deliveryProductDTOS = new ArrayList<>();
}
