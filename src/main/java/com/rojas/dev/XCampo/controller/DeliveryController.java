package com.rojas.dev.XCampo.controller;

import com.rojas.dev.XCampo.dto.DeliveryClientDTO;
import com.rojas.dev.XCampo.dto.DeliveryRuteDTO;
import com.rojas.dev.XCampo.entity.DeliveryProduct;
import com.rojas.dev.XCampo.service.Interface.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/delivery")
public class DeliveryController {

    @Autowired
    DeliveryService deliveryService;

    @PostMapping
    ResponseEntity<?> insertDelivery(@RequestBody DeliveryProduct delivery){
        return deliveryService.insertDelivery(delivery);
    }

    @PatchMapping("updateState")
    ResponseEntity<?> updateStateDelivery(@RequestBody DeliveryProduct delivery){
        return deliveryService.updateStateDelivery(delivery);
    }

    @PatchMapping("updateDeliveryMan")
    ResponseEntity<?> updateDeliveryMan(@RequestBody  DeliveryProduct delivery){
        return deliveryService.updateDeliveryMan(delivery);
    }

    @GetMapping("getByClient")
    ResponseEntity<?> getDeliveryByClientAndState(@RequestBody DeliveryClientDTO delivery){
        return  deliveryService.getDeliveryByClientAndState(delivery);
    }

    @GetMapping("getByDeliveryMan")
    ResponseEntity<?> getDeliveryByDeliveryManAndState(@RequestBody DeliveryProduct delivery){
        return deliveryService.getDeliveryByDeliveryManAndState(delivery);
    }

    @GetMapping("{id_delivery}")
    ResponseEntity<?> getDeliveryById(@PathVariable Long id_delivery){
        return deliveryService.getDeliveryById(id_delivery);
    }

    @GetMapping("rute")
    ResponseEntity<?> getDeliveryByRuteAndState(@RequestBody DeliveryRuteDTO locations){
        return deliveryService.getDeliveryByRuteAndState(locations);
    }

}
