package com.rojas.dev.XCampo.service.Interface;

import com.rojas.dev.XCampo.dto.*;
import com.rojas.dev.XCampo.entity.DeliveryProduct;
import com.rojas.dev.XCampo.enumClass.DeliveryProductState;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

public interface DeliveryService {

    ResponseEntity<?> insertDelivery(GetDeliveryProductDTO delivery);

    ResponseEntity<?> updateStateDelivery(DeliveryProduct delivery);

    ResponseEntity<?> updateDeliveryMan(DeliveryProduct delivery);

    ResponseEntity<?> getDeliveryByClientAndState(DeliveryClientDTO delivery);

    ResponseEntity<?> getDeliveryByDeliveryManAndState(DeliveryProduct delivery);

    ResponseEntity<?> getDeliveryById(Long id_delivery);

    GetDeliveryPdtForDlvManDTO getDeliveryByIdOrder(Long id_order);

    ResponseEntity<?> getDeliveryByRuteAndState(DeliveryRuteDTO request);

    Long countDeliveryAvailable(String state);

    List<GetDeliveryProductDTO> getAllDeliveryAvailable(String state);

    List<GetDeliveryPdtForDlvManDTO> getDeliveryForDlvMen(String state);

    List<DeliveryGroupedBySellerDTO> getGroupedDeliveries(String state);

    void updateStateDeliverYMatch(Long id);
}
