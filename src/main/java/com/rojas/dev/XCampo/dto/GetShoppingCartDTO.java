package com.rojas.dev.XCampo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetShoppingCartDTO {
    private Long idCart;
    private Long clientId;
    private String clientName;
    private boolean cartStatus;
    private LocalDate cartDateAdded;
    private List<CartItemDTO> cartItems = new ArrayList<>();
}
