package com.rojas.dev.XCampo.service.ServiceImp;

import com.rojas.dev.XCampo.dto.OrderDTO;
import com.rojas.dev.XCampo.entity.Order;
import com.rojas.dev.XCampo.enumClass.OrderState;
import com.rojas.dev.XCampo.exception.EntityNotFoundException;
import com.rojas.dev.XCampo.exception.InvalidDataException;
import com.rojas.dev.XCampo.repository.OrderRepository;
import com.rojas.dev.XCampo.repository.SellerRepository;
import com.rojas.dev.XCampo.service.Interface.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class OrderServiceImp implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private ShoppingCarServiceImp shoppingCarServiceImp;

    @Override
    public OrderDTO createNewOrder(OrderDTO orderDTO) {
        var shoppingCart = shoppingCarServiceImp
                .findByIdShoppingCard(orderDTO.getShoppingCartId().getIdCart());

        Order order = new Order();
        order.setDate(LocalDate.now());
        order.setHour(LocalTime.now());
        order.setState(OrderState.EN_ESPERA);
        order.setMessage(orderDTO.getMessage());

        order.setDelivery(orderDTO.getDelivery());
        order.setPrice_delivery(orderDTO.getPriceDelivery());

        order.setShoppingCart(shoppingCart);
        orderRepository.save(order);

        return convertToOrder(order);
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + id));
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToOrder)
                .toList();
    }

    @Override
    public List<OrderDTO> getOrdersByClient(Long clientId) {
        return orderRepository.findOrdersByClientId(clientId).stream()
                .map(this::convertToOrder)
                .toList();
    }

    @Override
    public OrderDTO updateOrderState(Long idOrder, String state) {
        if (!OrderState.contains(state)) throw new InvalidDataException("State is incorrect");

        var order = getOrderById(idOrder);
        order.setState(OrderState.valueOf(state.toUpperCase()));
        orderRepository.save(order);

        return convertToOrder(order);
    }

    @Override
    public Double calculateEarningsOrder(Long sellerId) {
        if (!sellerRepository.existsById(sellerId)) throw new EntityNotFoundException("Seller not exist whit ID: " + sellerId);
        var order =orderRepository.findOrdersBySellerId(OrderState.ACEPTADA, sellerId);

        return order != null ? calculateTotalEarnings(order) : 0.0;
    }

    private Double calculateTotalEarnings(Order order) {
        var orderDto = convertToOrder(order);
        return orderDto.getShoppingCartId().getTotalEarnings();
    }

    @Override
    public OrderDTO convertToOrder(Order order){
        var shoppingCartDTO = shoppingCarServiceImp.convertToShoppingCartDTO(order.getShoppingCart());

        return new OrderDTO(
                order.getId_order(),
                order.getState(),
                order.getMessage(),
                order.getDelivery(),
                order.getPrice_delivery(),
                shoppingCartDTO
        );
    }

}
