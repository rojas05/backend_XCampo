package com.rojas.dev.XCampo.controller;

import com.rojas.dev.XCampo.entity.Client;
import com.rojas.dev.XCampo.entity.DeliveryMan;
import com.rojas.dev.XCampo.service.Interface.ClientService;
import com.rojas.dev.XCampo.service.Interface.DeliveryManService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/deliveryMan")
public class DeliveryManController {
    @Autowired
    DeliveryManService deliveryManService;

    @PostMapping("{idRol}")
    ResponseEntity<?> insertDeliveryMan(@RequestBody DeliveryMan deliveryMan, @PathVariable Long idRol){
        return deliveryManService.insertDeliveryMan(deliveryMan, idRol);
    }
    @DeleteMapping("{id_deliveryMan}")
    public ResponseEntity<?> deleteDeliveryMan(@PathVariable Long id_deliveryMan){
        return deliveryManService.delete(id_deliveryMan);
    }


    @GetMapping("idUser/{id_user}")
    public ResponseEntity<?> getIdDeliveryManByIdUser(@PathVariable Long id_user){
        return deliveryManService.getIdDeliveryManByUser(id_user);
    }

    @GetMapping("{id_deliveryMan}")
    public ResponseEntity<?> getDeliveryManById(@PathVariable Long id_deliveryMan){
        return deliveryManService.getDeliveryManById(id_deliveryMan);
    }

    @PatchMapping
    public ResponseEntity<?> updateDeliveryMan(@RequestBody DeliveryMan deliveryMan){
        return deliveryManService.updateDeliveryMan(deliveryMan);
    }

}
