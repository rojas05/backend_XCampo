package com.rojas.dev.XCampo.service.Interface;

import com.rojas.dev.XCampo.dto.RequestCoordinatesDTO;
import org.springframework.http.ResponseEntity;

public interface DistanceService {

    int CalcularTarifa(RequestCoordinatesDTO request, Long idCart);

}
