package com.rojas.dev.XCampo.service.Interface;

import com.rojas.dev.XCampo.entity.DeliveryProduct;
import com.rojas.dev.XCampo.enumClass.DeliveryProductState;
import org.springframework.http.ResponseEntity;

public interface DeliveryService {

    ResponseEntity<?> insertDelivery(DeliveryProduct delivery);

    ResponseEntity<?> updateStateDelivery(Long id_delivery, DeliveryProductState state);

    ResponseEntity<?> updateDeliveryMan(Long id_delivery, Long id_deliveryMan);

    ResponseEntity<?> getDeliveryByClientAndState(Long id_client, DeliveryProductState state);

    ResponseEntity<?> getDeliveryByDeliveryManAndState(Long id_deliveryMan, DeliveryProductState state);

}
