package com.rojas.dev.XCampo.service.ServiceImp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rojas.dev.XCampo.dto.DeliveryClientDTO;
import com.rojas.dev.XCampo.dto.DeliveryRuteDTO;
import com.rojas.dev.XCampo.entity.DeliveryProduct;
import com.rojas.dev.XCampo.listeners.DeliveryEntityListener;
import com.rojas.dev.XCampo.repository.DeliveryRepository;
import com.rojas.dev.XCampo.service.Interface.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
public class DeliveryServiceImp implements DeliveryService {

    @Autowired
    DeliveryRepository deliveryRepository;

    private static KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    public  void init(KafkaTemplate<String,String> kafkaTemplate){
        DeliveryServiceImp.kafkaTemplate = kafkaTemplate;
    }


    @Override
    public ResponseEntity<?> insertDelivery(DeliveryProduct delivery) {
        try {
                deliveryRepository.save(delivery);
                URI location = ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(delivery.getID())
                        .toUri();
                return ResponseEntity.created(location).body(delivery);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Data integrity violation: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while saving the seller: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> updateStateDelivery(DeliveryProduct delivery) {
        try {
            deliveryRepository.updateState(delivery.getID(),delivery.getState());
            onDeliveryUpdate(delivery.getID());
            return ResponseEntity.ok().body("delivery update");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while update the delivery: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> updateDeliveryMan(DeliveryProduct delivery) {
       try {
           deliveryRepository.updateDeliveryMan(delivery.getID(),delivery.getDeliveryMan());

           return ResponseEntity.ok().body("delivery update");
       }catch (Exception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body("Error occurred while update the delivery: " + e.getMessage());
       }
    }

    @Override
    public ResponseEntity<?> getDeliveryByClientAndState(DeliveryClientDTO request) {
        try {
            System.out.println(request);
            Optional<List<DeliveryProduct>> result  = deliveryRepository.getDeliveryByClientAndState(request.getClient(),request.getState());
            if(result.isPresent()){
                return ResponseEntity.ok().body(result);
            }else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body("no dates");
            }
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while get the delivery: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getDeliveryByDeliveryManAndState(DeliveryProduct request) {
        try {
            System.out.println(request);
            Optional<List<DeliveryProduct>> result = deliveryRepository.getDeliveryByDeliveryManAndState(request.getDeliveryMan(),request.getState());
            if(result.isPresent()){
                return ResponseEntity.ok().body(result);
            }else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("no dates");
            }
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while get the delivery: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getDeliveryById(Long id_delivery) {
        try {
            Optional<DeliveryProduct> result = deliveryRepository.findById(id_delivery);
            if(result.isPresent()){
                return ResponseEntity.ok().body(result);
            }else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("no dates");
            }
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while get the delivery: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getDeliveryByRuteAndState(DeliveryRuteDTO request) {
        try {
            Optional<List<DeliveryProduct>> result = deliveryRepository.getDeliveryByRuteAndState(
                    request.getLocations(),
                    request.getState());
            if(result.isPresent()){
                return ResponseEntity.ok().body(result);
            }else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("no dates by rute" + request.getLocations());
            }
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while get the delivery: " + e.getMessage());
        }
    }

    void onDeliveryUpdate(Long deliveryId){
        try{
            Optional<DeliveryProduct> deliveryProduct = deliveryRepository.findById(deliveryId);
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String deliveryJson = mapper.writeValueAsString(deliveryProduct.get());
            kafkaTemplate.send("deliveryUpdate-notification", deliveryJson);
            System.out.println("evento enviado a Kafka");
        } catch (JsonProcessingException e) {
            System.err.println("ERROR ====>" + e);
        }
    }
}
