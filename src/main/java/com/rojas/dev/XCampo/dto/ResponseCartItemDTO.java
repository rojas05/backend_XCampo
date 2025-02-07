package com.rojas.dev.XCampo.dto;

import com.rojas.dev.XCampo.entity.Client;
import com.rojas.dev.XCampo.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCartItemDTO {
    private Long id_cart;
    private Client client;
    private Long items;
    private LocalDate dateAdded;
    private Long totalEarnings;
    private Order order;
}
