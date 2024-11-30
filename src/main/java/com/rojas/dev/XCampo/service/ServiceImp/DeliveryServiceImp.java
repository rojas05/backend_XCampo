package com.rojas.dev.XCampo.service.ServiceImp;

import com.rojas.dev.XCampo.entity.DeliveryProduct;
import com.rojas.dev.XCampo.enumClass.DeliveryProductState;
import com.rojas.dev.XCampo.service.Interface.DeliveryService;
import org.springframework.http.ResponseEntity;

public class DeliveryServiceImp implements DeliveryService {
    @Override
    public ResponseEntity<?> insertDelivery(DeliveryProduct delivery) {
        return null;
    }

    @Override
    public ResponseEntity<?> updateStateDelivery(Long id_delivery, DeliveryProductState state) {
        return null;
    }

    @Override
    public ResponseEntity<?> updateDeliveryMan(Long id_delivery, Long id_deliveryMan) {
        return null;
    }

    @Override
    public ResponseEntity<?> getDeliveryByClientAndState(Long id_client, DeliveryProductState state) {
        return null;
    }

    @Override
    public ResponseEntity<?> getDeliveryByDeliveryManAndState(Long id_deliveryMan, DeliveryProductState state) {
        return null;
    }
}
