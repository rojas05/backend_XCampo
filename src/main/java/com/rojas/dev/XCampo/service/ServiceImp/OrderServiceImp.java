package com.rojas.dev.XCampo.service.ServiceImp;

import com.rojas.dev.XCampo.dto.OrderDTO;
import com.rojas.dev.XCampo.entity.Order;
import com.rojas.dev.XCampo.entity.Seller;
import com.rojas.dev.XCampo.enumClass.OrderState;
import com.rojas.dev.XCampo.exception.EntityNotFoundException;
import com.rojas.dev.XCampo.repository.OrderRepository;
import com.rojas.dev.XCampo.repository.ProductRepository;
import com.rojas.dev.XCampo.repository.SellerRepository;
import com.rojas.dev.XCampo.service.Interface.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Executable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImp implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private ShoppingCarServiceImp shoppingCarServiceImp;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public OrderDTO createNewOrder(OrderDTO orderDTO) {
        var shoppingCart = shoppingCarServiceImp
                .findByIdShoppingCard(orderDTO.getShoppingCartId().getIdCart());

        shoppingCarServiceImp.updateStateCart(orderDTO.getShoppingCartId().getIdCart());

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
    public List<OrderDTO> getOrdersByClient(Long clientId, OrderState state) {
        if(state.equals(OrderState.FINALIZADA)){
            return orderRepository.findOrdersByClientId(clientId, state).stream()
                    .map(this::convertToOrder)
                    .toList();
        }

        return orderRepository.findOrdersByClientIdNext(clientId, OrderState.FINALIZADA).stream()
                .map(this::convertToOrder)
                .toList();
    }

    @Override
    public List<OrderDTO> getOrdersBySellerID(Long sellerId, String state) {
        if (!sellerRepository.existsById(sellerId))
            throw new EntityNotFoundException("Seller not exist whit ID: " + sellerId);

        OrderState orderState = OrderState.fromString(state)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order state: " + state));

        return orderRepository.findOrdersBySeller(orderState, sellerId).stream()
                .map(this::convertToOrder)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getOrdersState(String state) {

        OrderState orderState = OrderState.fromString(state)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order state: " + state));

        return orderRepository.findOrdersByState(orderState).stream()
                .map(this::convertToOrder)
                .collect(Collectors.toList());
    }

    @Override
    public Long getOrderCount(Long sellerId, String state) {
        if (!sellerRepository.existsById(sellerId))
            throw new EntityNotFoundException("Seller not exist whit ID: " + sellerId);

        OrderState orderState = OrderState.fromString(state)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order state: " + state));

        return orderRepository.countOrdersBySellerAndStatus(orderState, sellerId);
    }

    @Override
    public Long countSellersInOrders(Long orderId, String state) {
        if (!orderRepository.existsById(orderId))
            throw new EntityNotFoundException("Order not exist whit ID: " + orderId);

        OrderState orderState = OrderState.fromString(state)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order state: " + state));

        return orderRepository.countSellersInOrders(orderState, orderId);
    }

    @Override
    public OrderDTO updateOrderState(Long idOrder, String state) {
        OrderState orderState = OrderState.fromString(state)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order state: " + state));

        var order = getOrderById(idOrder);
        order.setState(orderState);

        if (orderState == OrderState.LISTA_ENVIAR) order.setDelivery(true);

        return convertToOrder(orderRepository.save(order));
    }

    @Override
    public Double calculateEarningsOrder(Long sellerId) {
        if (!sellerRepository.existsById(sellerId))
            throw new EntityNotFoundException("Seller not exist whit ID: " + sellerId);

        var order = orderRepository.findOrdersBySellerId(OrderState.ACEPTADA, sellerId);

        return order != null ? calculateTotalEarnings(order) : 0.0;
    }

    private Double calculateTotalEarnings(Order order) {
        var orderDto = convertToOrder(order);
        return orderDto.getShoppingCartId().getTotalEarnings();
    }

    @Override
    public OrderDTO convertToOrder(Order order) {
        var shoppingCartDTO = shoppingCarServiceImp.convertToShoppingCartDTO(order.getShoppingCart());

        return new OrderDTO(
                order.getId_order(),
                order.getState(),
                order.getMessage(),
                order.getDelivery(),
                order.getPrice_delivery(),
                shoppingCartDTO,
                order.getDate()
        );
    }

    @Override
    public List<String> getNfsSellersByOrderId(Long id) {
        try {
            return orderRepository.getNfsSellersByOrderId(id);
        }catch (Exception e){
            System.err.println("ERROR EN CONSULTA =====> "+e);
            return null;
        }
    }

    @Override
    public Long getIdClientByOrderId(Long idOrder) {
        existsOrderId(idOrder);
        return orderRepository.getIdClientByIdOrder(idOrder);
    }

    @Override
    public Long getIdSellerByOrderId(Long idOrder) {
        existsOrderId(idOrder);
        return orderRepository.getIdSellerByIdOrder(idOrder);
    }

    @Override
    public Long countSalesByProductId(Long idProduct) {
        if (!productRepository.existsById(idProduct))
            throw new EntityNotFoundException("Product not exist whit ID: " + idProduct);

        return orderRepository.countSalesByProductId(idProduct);
    }

    /**
     * funcion para exponer las tiendas en las que un cliente a comprado
     * @param id
     * @return lista de tiendas
     */
    @Override
    public ResponseEntity<?> getSellersFavorite(Long id) {
        try {
            List<Seller> result = orderRepository.getSellerByOrder(id);

            if(!result.isEmpty())
                return ResponseEntity.ok(result);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No seller"+result);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    public String getDestinyClient(Long idOrder) {
        existsOrderId(idOrder);
        return orderRepository.getDestinyClient(idOrder);
    }

    public void existsOrderId(Long idOrder) {
        if (!orderRepository.existsById(idOrder))
            throw new EntityNotFoundException("Order not exist whit ID: " + idOrder);
    }

}
