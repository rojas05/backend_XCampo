package com.rojas.dev.XCampo.service.ServiceImp;

import com.rojas.dev.XCampo.entity.OrderState;
import com.rojas.dev.XCampo.service.Interface.OrderService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderServiceImp implements OrderService {
    @Override
    public void updateState(Long idOrder, OrderState state) {

    }

    @Override
    public BigDecimal calculateEarningsOrder(Long Id) {
        return null;
    }
}
