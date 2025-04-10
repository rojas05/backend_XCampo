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

/**
 * controlador para envios
 */
@Controller
@RequestMapping("/delivery")
public class DeliveryController {

    @Autowired
    DeliveryService deliveryService;

    /**
     * insertar un nuevo envio
     * @param delivery
     * @return estado y envio insertado
     */
    @PostMapping
    ResponseEntity<?> insertDelivery(@RequestBody GetDeliveryProductDTO delivery){
        return deliveryService.insertDelivery(delivery);
    }

    /**
     * actualizar el estdo del envio
     * @param delivery
     * @return estado http
     */
    @PatchMapping("updateState")
    ResponseEntity<?> updateStateDelivery(@RequestBody DeliveryProduct delivery){
        return deliveryService.updateStateDelivery(delivery);
    }

    /**
     * endPoint para actualizar un envio
     * @param delivery
     * @return
     */
    @PatchMapping("updateDeliveryMan")
    ResponseEntity<?> updateDeliveryMan(@RequestBody  DeliveryProduct delivery){
        return deliveryService.updateDeliveryMan(delivery);
    }

    /**
     * enPoint para listar un envio por el cliente
     * @param delivery
     * @return envio
     */
    @GetMapping("getByClient")
    ResponseEntity<?> getDeliveryByClientAndState(@RequestBody DeliveryClientDTO delivery){
        return  deliveryService.getDeliveryByClientAndState(delivery);
    }

    /**
     * endPint para retornar los envios filtrando por estado y repartidor
     * @param delivery
     * @return lista de envios
     */
    @GetMapping("getByDeliveryMan")
    ResponseEntity<?> getDeliveryByDeliveryManAndState(@RequestBody DeliveryProduct delivery){
        return deliveryService.getDeliveryByDeliveryManAndState(delivery);
    }

    /**
     * obtener el envio por su id
     * @param id_delivery
     * @return envio
     */
    @GetMapping("{id_delivery}")
    ResponseEntity<?> getDeliveryById(@PathVariable Long id_delivery){
        return deliveryService.getDeliveryById(id_delivery);
    }


    @GetMapping("/found/{id_delivery}")
    ResponseEntity<?> getDeliveryByIdForDlvMan(@PathVariable Long id_delivery){
        var getDelivery = deliveryService.getDeliveryByIdForDlvMan(id_delivery);
        return ResponseEntity.status(HttpStatus.FOUND).body(getDelivery);
    }


    /**
     * obtener el envio segun el id de la orden
     * @param id_delivery
     * @return envio
     */

    @GetMapping("/idOrder/{id_delivery}")
    ResponseEntity<?> getDeliveryOrderById(@PathVariable Long id_delivery){
        var getOrder = deliveryService.getDeliveryByIdOrder(id_delivery);
        return ResponseEntity.status(HttpStatus.OK).body(getOrder);
    }

    /**
     * rettorna los envios correspondientes a una ruta
     * @param locations
     * @return lista de envios
     */
    @GetMapping("rute")
    ResponseEntity<?> getDeliveryByRuteAndState(@RequestBody DeliveryRuteDTO locations){
        return deliveryService.getDeliveryByRuteAndState(locations);
    }

    /**
     * obtiene los envios segun su estado
     * @param state
     * @return lista de envios
     */
    @GetMapping("/getList/{state}")
    ResponseEntity<?> getAllDeliverState(@PathVariable String state){
        List<GetDeliveryProductDTO> allDelivery = deliveryService.getAllDeliveryAvailable(state);
        return ResponseEntity.status(HttpStatus.FOUND).body(allDelivery);
    }

    /**
     * retorna la cantidad de envios segun su estado
     * @param state
     * @return cantidad de envios con el estado
     */
    @GetMapping("/getTotalAvailable/{state}/{municipio}")
    ResponseEntity<?> countDeliveryAvalible(@PathVariable String state, @PathVariable String municipio){
        Long allDelivery = deliveryService.countDeliveryAvailable(state, municipio);
        return ResponseEntity.status(HttpStatus.OK).body(allDelivery);
    }

    /**
     * obtiene los envios segun su estado
     * @param state
     * @return lista de envios
     */
    @GetMapping("/getAll/{state}/{municipio}")
    ResponseEntity<?> getAllDeliverDTOState(@PathVariable String state, @PathVariable String municipio){
        List<GetDeliveryPdtForDlvManDTO> allDelivery = deliveryService.getDeliveryForDlvMen(state, municipio);
        return ResponseEntity.status(HttpStatus.FOUND).body(allDelivery);
    }

    /**
     * obtiene los envios segun su estado
     * @param state
     * @return lista de envios
     */
    @GetMapping("/getListGroup/{state}/{departament}")
    public ResponseEntity<?> getGroupedDeliveries(
            @PathVariable String state,
            @PathVariable String departament,
            @RequestParam List<String> municipio
    ) {
        if (municipio.size() > 3) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Solo se permiten máximo 3 municipios");
        }

        var allDelivery = deliveryService.getGroupedDeliveries(state, departament, municipio);
        return ResponseEntity.status(HttpStatus.OK).body(allDelivery);
    }

}
