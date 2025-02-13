package com.rojas.dev.XCampo.service.ServiceImp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rojas.dev.XCampo.dto.DeliveryClientDTO;
import com.rojas.dev.XCampo.dto.DeliveryRuteDTO;
import com.rojas.dev.XCampo.dto.GetDeliveryProductDTO;
import com.rojas.dev.XCampo.entity.DeliveryProduct;
import com.rojas.dev.XCampo.exception.EntityNotFoundException;
import com.rojas.dev.XCampo.repository.DeliveryManRepository;
import com.rojas.dev.XCampo.repository.DeliveryRepository;
import com.rojas.dev.XCampo.repository.OrderRepository;
import com.rojas.dev.XCampo.service.Interface.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class DeliveryServiceImp implements DeliveryService {

    @Autowired
    DeliveryRepository deliveryRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DeliveryManRepository deliveryManRepository;

    private static KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    public void init(KafkaTemplate<String,String> kafkaTemplate){
        DeliveryServiceImp.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public ResponseEntity<?> insertDelivery(GetDeliveryProductDTO delivery) {
        try {
            var idOrder = delivery.getOrderId();
            var order = orderRepository.findById(idOrder)
                    .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + idOrder));
            var deliveryMan = deliveryManRepository.findById(delivery.getDeliveryManId())
                    .orElseThrow(() -> new EntityNotFoundException("Delivery man not found with ID: " + delivery.getDeliveryManId()));

            DeliveryProduct deliveryProduct = new DeliveryProduct();
            deliveryProduct.setDate(LocalDate.now());
            deliveryProduct.setAvailable(delivery.getAvailable());
            deliveryProduct.setState(delivery.getState());
            deliveryProduct.setStartingPoint(delivery.getStartingPoint());
            deliveryProduct.setDestiny(delivery.getDestiny());
            deliveryProduct.setOrder(order);
            deliveryProduct.setDeliveryMan(deliveryMan);

            deliveryRepository.save(deliveryProduct);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(delivery.getId())
                    .toUri();
            return ResponseEntity.created(location).body(delivery);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Data integrity violation: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> updateStateDelivery(DeliveryProduct delivery) {
        deliveryRepository.updateState(delivery.getID(),delivery.getState());
        onDeliveryUpdate(delivery.getID());
        return ResponseEntity.ok().body("delivery update");
    }

    @Override
    public ResponseEntity<?> updateDeliveryMan(DeliveryProduct delivery) {
        deliveryRepository.updateDeliveryMan(delivery.getID(),delivery.getDeliveryMan());

        return ResponseEntity.ok().body("delivery update");
    }

    @Override
    public ResponseEntity<?> getDeliveryByClientAndState(DeliveryClientDTO request) {
        System.out.println(request);
        Optional<List<DeliveryProduct>> result  = deliveryRepository.getDeliveryByClientAndState(request.getClient(),request.getState());

        if(result.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("no dates");
        }

        return ResponseEntity.ok().body(result);
    }

    @Override
    public ResponseEntity<?> getDeliveryByDeliveryManAndState(DeliveryProduct request) {
        System.out.println(request);
        Optional<List<DeliveryProduct>> result = deliveryRepository.getDeliveryByDeliveryManAndState(request.getDeliveryMan(),request.getState());
        if(result.isPresent()){
            return ResponseEntity.ok().body(result);
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("no dates");
        }
    }

    @Override
    public ResponseEntity<?> getDeliveryById(Long id_delivery) {
        Optional<DeliveryProduct> result = deliveryRepository.findById(id_delivery);
        if(result.isPresent()){
            return ResponseEntity.ok().body(result);
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("no dates");
        }
    }

    @Override
    public ResponseEntity<?> getDeliveryByRuteAndState(DeliveryRuteDTO request) {
        Optional<List<DeliveryProduct>> result = deliveryRepository.getDeliveryByRuteAndState(
                request.getLocations(),
                request.getState());
        if(result.isPresent()){
            return ResponseEntity.ok().body(result);
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("no dates by rute" + request.getLocations());
        }
    }

    void onDeliveryUpdate(Long deliveryId){
        try{
            DeliveryProduct deliveryProduct = deliveryRepository.findById(deliveryId)
                    .orElseThrow(() -> new EntityNotFoundException("Delivery products not found with ID: " + deliveryId));
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String deliveryJson = mapper.writeValueAsString(deliveryProduct);
            kafkaTemplate.send("deliveryUpdate-notification", deliveryJson);
            System.out.println("Event enviado a Kafka");
        } catch (JsonProcessingException e) {
            System.err.println("ERROR ====>" + e);
        }
    }
}
