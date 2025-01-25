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
    private Integer productPrice;
    private Integer productStock;
    private Boolean productState;
    // date cart item
    private int itemQuantity;
    private double itemUnitPrice;
}
