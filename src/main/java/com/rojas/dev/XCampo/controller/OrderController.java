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

    /**
     * crear una nueva orden
     * @param orderDTO
     * @return estado http y nueva orden
     */
    @PostMapping()
    public ResponseEntity<?> createNewOrder(@RequestBody OrderDTO orderDTO){
        var order = orderService.createNewOrder(orderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    /**
     * obtener orden por id
     * @param id
     * @return orden
     */
    @GetMapping("/getId/{id}")
    public ResponseEntity<?> getOrderId(@PathVariable Long id){
        var order = orderService.getOrderById(id);
        var orderDTO = orderService.convertToOrder(order);
        return ResponseEntity.status(HttpStatus.OK).body(orderDTO);
    }

    /**
     * listar todas las ordenes
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<?> getAllOrder(){
        var order = orderService.getAllOrders();
        return ResponseEntity.status(HttpStatus.FOUND).body(order);
    }

    /**
     * listar orden por
     * @param idClient
     * @param state
     * @return orden
     */
    @GetMapping("/get/{idClient}/{state}")
    public ResponseEntity<?> getOrderByClient(@PathVariable Long idClient, @PathVariable OrderState state){
        var order = orderService.getOrdersByClient(idClient,state);
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }

    /**
     * filtar por
     * @param sellerId
     * @param state
     * @return orden
     */
    @GetMapping("/getSeller/{sellerId}/state/{state}")
    public ResponseEntity<?> getOrderBySeller(@PathVariable Long sellerId, @PathVariable String state ){
        var order = orderService.getOrdersBySellerID(sellerId, state);
        return ResponseEntity.status(HttpStatus.FOUND).body(order);
    }

    /**
     * filtar por
     * @param state
     * @return orden
     */
    @GetMapping("/state/{state}")
    public ResponseEntity<?> getOrderByState(@PathVariable String state ){
        var order = orderService.getOrdersState(state);
        return ResponseEntity.status(HttpStatus.FOUND).body(order);
    }

    /**
     * listar filtrando por
     * @param sellerId
     * @param state
     * @return catidad de ordenes
     */
    @GetMapping("/count/getSeller/{sellerId}/state/{state}")
    public ResponseEntity<?> getOrderCountBySeller(@PathVariable Long sellerId, @PathVariable String state ){
        Long order = orderService.getOrderCount(sellerId, state);
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }

    /**
     * actualiza el estado de una orden
     * @param idOrder
     * @param state
     * @return estado http
     */
    @PatchMapping("{idOrder}/{state}")
    public ResponseEntity<?> updateOrderState(@PathVariable Long idOrder, @PathVariable String state){
        var order = orderService.updateOrderState(idOrder, state);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    /**
     * listar las ganancias
     * @param idSeller
     * @return ganancias
     */
    @GetMapping("/getGanacias/{idSeller}")
    public ResponseEntity<?> getGanacias(@PathVariable Long idSeller){
        var order = orderService.calculateEarningsOrder(idSeller);
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }

}
