package com.rojas.dev.XCampo.service.Interface;

import com.rojas.dev.XCampo.dto.OrderDTO;
import com.rojas.dev.XCampo.entity.Order;
import com.rojas.dev.XCampo.enumClass.OrderState;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {

    OrderDTO createNewOrder(OrderDTO orderDTO);

    Order getOrderById(Long id);

    List<OrderDTO> getAllOrders();

    List<OrderDTO> getOrdersByClient(Long clientId);

    OrderDTO updateOrderState(Long idOrder, String state);

    Double calculateEarningsOrder(Long Id);

    OrderDTO convertToOrder(Order order);

}
