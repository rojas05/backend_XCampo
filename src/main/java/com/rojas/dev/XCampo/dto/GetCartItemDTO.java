package com.rojas.dev.XCampo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class GetCartItemDTO {
    // date Cart
    private Long idCartItem;
    private Long cartId;
    private Boolean cartStatus;
    private LocalDate cartDateAdded;
    // date product
    private Long productId;
    private String productName;
    private String img;
    private Integer productPrice;
    private Long productStock;
    private Boolean productState;
    // date cart item
    private Long itemQuantity;
    private double itemUnitPrice;
}
