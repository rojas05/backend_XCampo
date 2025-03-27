package com.rojas.dev.XCampo.controller;

import com.rojas.dev.XCampo.dto.DeliveryClientDTO;
import com.rojas.dev.XCampo.dto.DeliveryRuteDTO;
import com.rojas.dev.XCampo.dto.GetDeliveryPdtForDlvManDTO;
import com.rojas.dev.XCampo.dto.GetDeliveryProductDTO;
import com.rojas.dev.XCampo.entity.DeliveryProduct;
import com.rojas.dev.XCampo.service.Interface.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/delivery")
public class DeliveryController {

    @Autowired
    DeliveryService deliveryService;

    @PostMapping
    ResponseEntity<?> insertDelivery(@RequestBody GetDeliveryProductDTO delivery){
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

    @GetMapping("/found/{id_delivery}")
    ResponseEntity<?> getDeliveryByIdForDlvMan(@PathVariable Long id_delivery){
        var getDelivery = deliveryService.getDeliveryByIdForDlvMan(id_delivery);
        return ResponseEntity.status(HttpStatus.FOUND).body(getDelivery);
    }

    @GetMapping("/idOrder/{id_delivery}")
    ResponseEntity<?> getDeliveryOrderById(@PathVariable Long id_delivery){
        var getOrder = deliveryService.getDeliveryByIdOrder(id_delivery);
        return ResponseEntity.status(HttpStatus.OK).body(getOrder);
    }

    @GetMapping("rute")
    ResponseEntity<?> getDeliveryByRuteAndState(@RequestBody DeliveryRuteDTO locations){
        return deliveryService.getDeliveryByRuteAndState(locations);
    }

    @GetMapping("/getList/{state}")
    ResponseEntity<?> getAllDeliverState(@PathVariable String state){
        List<GetDeliveryProductDTO> allDelivery = deliveryService.getAllDeliveryAvailable(state);
        return ResponseEntity.status(HttpStatus.FOUND).body(allDelivery);
    }

    @GetMapping("/getTotalAvailable/{state}")
    ResponseEntity<?> countDeliveryAvalible(@PathVariable String state){
        Long allDelivery = deliveryService.countDeliveryAvailable(state);
        return ResponseEntity.status(HttpStatus.OK).body(allDelivery);
    }

    @GetMapping("/getAll/{state}")
    ResponseEntity<?> getAllDeliverDTOState(@PathVariable String state){
        List<GetDeliveryPdtForDlvManDTO> allDelivery = deliveryService.getDeliveryForDlvMen(state);
        return ResponseEntity.status(HttpStatus.FOUND).body(allDelivery);
    }

    @GetMapping("/getListGroup/{state}")
    ResponseEntity<?> getGroupedDeliveries(@PathVariable String state){
        var allDelivery = deliveryService.getGroupedDeliveries(state);
        return ResponseEntity.status(HttpStatus.FOUND).body(allDelivery);
    }

}
