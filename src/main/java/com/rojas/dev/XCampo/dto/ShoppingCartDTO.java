package com.rojas.dev.XCampo.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ShoppingCartDTO {
    private Long clientId;
    private Long itemId;
    private boolean status = false;
    private LocalDate dateAdded;
}
