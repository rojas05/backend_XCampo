package com.rojas.dev.XCampo.service.Interface;

import com.rojas.dev.XCampo.entity.DeliveryMan;
import org.springframework.http.ResponseEntity;

public interface DeliveryManService {
    ResponseEntity<?> insertDeliveryMan(DeliveryMan deliveryMan, Long idRol);

    ResponseEntity<?> getIdDeliveryManByUser(Long deliveryMan_id);

    ResponseEntity<?> updateDeliveryMan(DeliveryMan deliveryMan);

    ResponseEntity<?> getDeliveryManById(Long deliveryMan_id);

    ResponseEntity<?> delete(Long deliveryMan_id);
}
