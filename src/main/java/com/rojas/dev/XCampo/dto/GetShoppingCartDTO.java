package com.rojas.dev.XCampo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private double totalEarnings = 0;
    private Set<CartItemDTO> cartItems = new HashSet<>();
}
