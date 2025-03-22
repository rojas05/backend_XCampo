package com.rojas.dev.XCampo.controller;

import com.rojas.dev.XCampo.entity.DeliveryMan;
import com.rojas.dev.XCampo.service.Interface.DeliveryManService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/deliveryman")
public class DeliveryManController {

    @Autowired
    DeliveryManService deliveryManService;

    /**
     * inserta un repartidor
     * @param deliveryMan
     * @param idRol
     * @return repartidor guardado y estado http
     */
    @PostMapping("{idRol}")
    ResponseEntity<?> insertDeliveryMan(@RequestBody DeliveryMan deliveryMan, @PathVariable Long idRol){
        return deliveryManService.insertDeliveryMan(deliveryMan, idRol);
    }

    /**
     * elimina un repartidor
     * @param id_deliveryMan
     * @return estado http
     */
    @DeleteMapping("{id_deliveryMan}")
    public ResponseEntity<?> deleteDeliveryMan(@PathVariable Long id_deliveryMan){
        return deliveryManService.delete(id_deliveryMan);
    }

    /**
     * optiene el id del repartidor
     * @param id_user
     * @return id repartidor
     */
    @GetMapping("idUser/{id_user}")
    public ResponseEntity<?> getIdDeliveryManByIdUser(@PathVariable Long id_user){
        return deliveryManService.getIdDeliveryManByUser(id_user);
    }

    /**
     * obtiene el repartidor
     * @param id_deliveryMan
     * @return repartidor
     */
    @GetMapping("{id_deliveryMan}")
    public ResponseEntity<?> getDeliveryManById(@PathVariable Long id_deliveryMan){
        return deliveryManService.getDeliveryManById(id_deliveryMan);
    }

    /**
     * actualiza la informacion de repartidor
     * @param deliveryMan
     * @return
     */
    @PatchMapping
    public ResponseEntity<?> updateDeliveryMan(@RequestBody DeliveryMan deliveryMan){
        return deliveryManService.updateDeliveryMan(deliveryMan);
    }

}
