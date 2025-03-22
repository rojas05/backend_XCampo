package com.rojas.dev.XCampo.controller;

import com.rojas.dev.XCampo.dto.RequestCoordinatesDTO;
import com.rojas.dev.XCampo.service.Interface.DistanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * controlador para las tarifas de los repartidores
 */
@RestController
@RequestMapping("/tarifa")
public class TarifaController {

    @Autowired
    DistanceService distanceService;

    /**
     * calcula el precio de un envio
     * @param request
     * @param id
     * @return
     */
    @PostMapping("{id}")
    public ResponseEntity<?> tarifa(@RequestBody RequestCoordinatesDTO request, @PathVariable Long id){
        int tarifa =  distanceService.CalcularTarifa(request,id);
        return ResponseEntity.ok(tarifa);
    }
}
