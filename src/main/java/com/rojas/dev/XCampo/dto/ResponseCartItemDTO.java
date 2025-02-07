package com.rojas.dev.XCampo.dto;

import com.rojas.dev.XCampo.entity.Client;
import com.rojas.dev.XCampo.entity.Order;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ResponseCartItemDTO {
    private Long id_cart;
    private Client client;
    private Long items;
    private LocalDate dateAdded;
    private Double totalEarnings;
    private Order order;
}
