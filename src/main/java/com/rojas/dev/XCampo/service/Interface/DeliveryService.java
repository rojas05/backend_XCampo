package com.rojas.dev.XCampo.service.Interface;

import com.rojas.dev.XCampo.dto.DeliveryClientDTO;
import com.rojas.dev.XCampo.dto.DeliveryRuteDTO;
import com.rojas.dev.XCampo.entity.DeliveryProduct;
import org.springframework.http.ResponseEntity;

public interface DeliveryService {

    ResponseEntity<?> insertDelivery(DeliveryProduct delivery);

    ResponseEntity<?> updateStateDelivery(DeliveryProduct delivery);

    ResponseEntity<?> updateDeliveryMan(DeliveryProduct delivery);

    ResponseEntity<?> getDeliveryByClientAndState(DeliveryClientDTO delivery);

    ResponseEntity<?> getDeliveryByDeliveryManAndState(DeliveryProduct delivery);

    ResponseEntity<?> getDeliveryById(Long id_delivery);

    ResponseEntity<?> getDeliveryByRuteAndState(DeliveryRuteDTO request);
}
