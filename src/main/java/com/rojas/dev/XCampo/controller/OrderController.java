package com.rojas.dev.XCampo.controller;

import com.rojas.dev.XCampo.dto.OrderDTO;
import com.rojas.dev.XCampo.enumClass.OrderState;
import com.rojas.dev.XCampo.service.Interface.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping()
    public ResponseEntity<?> createNewOrder(@RequestBody OrderDTO orderDTO){
        var order = orderService.createNewOrder(orderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/getId/{id}")
    public ResponseEntity<?> getOrderId(@PathVariable Long id){
        var order = orderService.getOrderById(id);
        var orderDTO = orderService.convertToOrder(order);
        return ResponseEntity.status(HttpStatus.FOUND).body(orderDTO);
    }

    @GetMapping()
    public ResponseEntity<?> getAllOrder(){
        var order = orderService.getAllOrders();
        return ResponseEntity.status(HttpStatus.FOUND).body(order);
    }

    @GetMapping("/getClient/{idClient}")
    public ResponseEntity<?> getOrderByClient(@PathVariable Long idClient){
        var order = orderService.getOrdersByClient(idClient);
        return ResponseEntity.status(HttpStatus.FOUND).body(order);
    }

    @PatchMapping("{idOrder}/{state}")
    public ResponseEntity<?> updateOrderState(@PathVariable Long idOrder, @PathVariable String state){
        var order = orderService.updateOrderState(idOrder, state);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/getGanacias/{idSeller}")
    public ResponseEntity<?> getGanacias(@PathVariable Long idSeller){
        var order = orderService.calculateEarningsOrder(idSeller);
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }

}
