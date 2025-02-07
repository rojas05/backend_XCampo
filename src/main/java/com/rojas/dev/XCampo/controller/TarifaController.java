package com.rojas.dev.XCampo.controller;

import com.rojas.dev.XCampo.dto.RequestCoordinatesDTO;
import com.rojas.dev.XCampo.service.Interface.DistanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tarifa")
public class TarifaController {

    @Autowired
    DistanceService distanceService;

    @PostMapping("{id}")
    public ResponseEntity<?> tarifa(@RequestBody RequestCoordinatesDTO request, @PathVariable Long id){
        return distanceService.CalcularTarifa(request,id);
    }
}
