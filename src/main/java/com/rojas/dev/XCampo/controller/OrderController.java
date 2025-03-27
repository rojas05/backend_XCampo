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
        return ResponseEntity.status(HttpStatus.OK).body(orderDTO);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllOrder(){
        var order = orderService.getAllOrders();
        return ResponseEntity.status(HttpStatus.FOUND).body(order);
    }

    @GetMapping("/get/{idClient}/{state}")
    public ResponseEntity<?> getOrderByClient(@PathVariable Long idClient, @PathVariable OrderState state){
        var order = orderService.getOrdersByClient(idClient,state);
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }

    @GetMapping("/getSeller/{sellerId}/state/{state}")
    public ResponseEntity<?> getOrderBySeller(@PathVariable Long sellerId, @PathVariable String state ){
        var order = orderService.getOrdersBySellerID(sellerId, state);
        return ResponseEntity.status(HttpStatus.FOUND).body(order);
    }

    @GetMapping("/state/{state}")
    public ResponseEntity<?> getOrderByState(@PathVariable String state ){
        var order = orderService.getOrdersState(state);
        return ResponseEntity.status(HttpStatus.FOUND).body(order);
    }

    @GetMapping("/count/getSeller/{sellerId}/state/{state}")
    public ResponseEntity<?> getOrderCountBySeller(@PathVariable Long sellerId, @PathVariable String state ){
        Long order = orderService.getOrderCount(sellerId, state);
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }

    @PatchMapping("{idOrder}/{state}")
    public ResponseEntity<?> updateOrderState(@PathVariable Long idOrder, @PathVariable String state){
        var order = orderService.updateOrderState(idOrder, state);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/getGanancias/{idSeller}")
    public ResponseEntity<?> getGanacias(@PathVariable Long idSeller){
        var order = orderService.calculateEarningsOrder(idSeller);
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }

    @GetMapping("/getClient/{idOrder}")
    public ResponseEntity<?> getIdClientByOrderId(@PathVariable Long idOrder){
        var order = orderService.getIdClientByOrderId(idOrder);
        return ResponseEntity.status(HttpStatus.FOUND).body(order);
    }

}
