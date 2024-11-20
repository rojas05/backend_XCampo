package com.rojas.dev.XCampo.service.Interface;

import com.rojas.dev.XCampo.enumClass.OrderState;

import java.math.BigDecimal;

public interface OrderService {

    // estado en aceptado para la ganacia
    void updateState(Long idOrder, OrderState state);

    // Calcular gancacis de compra
    BigDecimal calculateEarningsOrder(Long Id);

}
