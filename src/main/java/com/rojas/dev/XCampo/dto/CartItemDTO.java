package com.rojas.dev.XCampo.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CartItemDTO {
    private Long cardId;
    private Long productId;
    private int quantity;
    private double unitPrice;
    private LocalDateTime dateAdded;
}
