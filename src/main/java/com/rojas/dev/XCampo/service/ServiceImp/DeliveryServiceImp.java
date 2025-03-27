package com.rojas.dev.XCampo.service.ServiceImp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rojas.dev.XCampo.dto.*;
import com.rojas.dev.XCampo.entity.DeliveryProduct;
import com.rojas.dev.XCampo.entity.Order;
import com.rojas.dev.XCampo.enumClass.DeliveryProductState;
import com.rojas.dev.XCampo.exception.EntityNotFoundException;
import com.rojas.dev.XCampo.repository.DeliveryManRepository;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeliveryServiceImp implements DeliveryService {

    @Autowired
    DeliveryRepository deliveryRepository;

    @Autowired
    private OrderServiceImp orderRepository;

    @Autowired
    private DeliveryManRepository deliveryManRepository;

    @Autowired
    private DistanceServiceImp distanceServiceImp;

    private static KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    public void init(KafkaTemplate<String,String> kafkaTemplate){
        DeliveryServiceImp.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public ResponseEntity<?> insertDelivery(GetDeliveryProductDTO delivery) {
        try {
            var idOrder = delivery.getOrderId();
            var order = orderRepository.getOrderById(idOrder);

            var idShoppingCart = order.getShoppingCart().getId_cart();
            delivery.setDestiny(orderRepository.getDestinyClient(idShoppingCart));

           /*
            var deliveryMan = deliveryManRepository.findById(delivery.getDeliveryManId())
                    .orElseThrow(() -> new EntityNotFoundException("Delivery man not found with ID: " + delivery.getDeliveryManId()));
            */
            DeliveryProduct deliveryProduct = new DeliveryProduct();
            deliveryProduct.setDate(LocalDate.now());
            deliveryProduct.setAvailable(delivery.getAvailable());
            deliveryProduct.setState(delivery.getState());
            deliveryProduct.setStartingPoint(delivery.getStartingPoint());
            deliveryProduct.setDestiny(delivery.getDestiny());
            deliveryProduct.setOrder(order);
            //deliveryProduct.setDeliveryMan(deliveryMan);

            deliveryRepository.save(deliveryProduct);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{idDelivery}")
                    .buildAndExpand(deliveryProduct.getId())
                    .toUri();

            return ResponseEntity.created(location).body(convertDeliveryProductsDTO(deliveryProduct));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Data integrity violation: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> updateStateDelivery(DeliveryProduct delivery) {
            deliveryRepository.updateState(delivery.getId(), delivery.getState());
        onDeliveryUpdate(delivery.getId());
        return ResponseEntity.ok().body("delivery update");
    }

    @Override
    public ResponseEntity<?> updateDeliveryMan(DeliveryProduct delivery) {
        deliveryRepository.updateDeliveryMan(delivery.getId(), delivery.getDeliveryMan());

        return ResponseEntity.ok().body("delivery update");
    }

    @Override
    public ResponseEntity<?> getDeliveryByClientAndState(DeliveryClientDTO request) {
        System.out.println(request);
        Optional<List<DeliveryProduct>> result  = deliveryRepository.getDeliveryByClientAndState(request.getClient(),request.getState());

        if(result.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("no dates");
        }

        return ResponseEntity.ok().body(result.get());
    }

    @Override
    public ResponseEntity<?> getDeliveryByDeliveryManAndState(DeliveryProduct request) {
        System.out.println(request);
        Optional<List<DeliveryProduct>> result = deliveryRepository.getDeliveryByDeliveryManAndState(request.getDeliveryMan(),request.getState());
        if(result.isPresent()){
            return ResponseEntity.ok().body(result.get());
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("no dates");
        }
    }

    @Override
    public ResponseEntity<?> getDeliveryById(Long id_delivery) {
        Optional<DeliveryProduct> result = deliveryRepository.findById(id_delivery);
        if(result.isPresent()){
            return ResponseEntity.ok().body(result.get());
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("no dates");
        }
    }

    @Override
    public GetDeliveryPdtForDlvManDTO getDeliveryByIdForDlvMan(Long idDelivery) {
        GetDeliveryPdtForDlvManDTO getDelivery = deliveryRepository.getDeliveryByIdForDlvMan(idDelivery);
        return convertDeliveryPdtForDeliveryMan(getDelivery);
    }

    @Override
    public GetDeliveryPdtForDlvManDTO getDeliveryByIdOrder(Long id_order) {
        GetDeliveryPdtForDlvManDTO getOrder = deliveryRepository.getDeliveryOrderIdDTO(id_order);
        return convertDeliveryPdtForDeliveryMan(getOrder);
    }

    @Override
    public ResponseEntity<?> getDeliveryByRuteAndState(DeliveryRuteDTO request) {
        Optional<List<DeliveryProduct>> result = deliveryRepository.getDeliveryByRuteAndState(
                request.getLocations(),
                request.getState());
        if(result.isPresent()){
            return ResponseEntity.ok().body(result.get());
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("no dates by rute" + request.getLocations());
        }
    }

    @Override
    public Long countDeliveryAvailable(String state) {
        DeliveryProductState deliveryState = DeliveryProductState.fromStringDeliveryState(state)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order state: " + state));

        return deliveryRepository.countDeliveryAvailable(deliveryState);
    }

    @Override
    public List<GetDeliveryProductDTO> getAllDeliveryAvailable(String state) {
        DeliveryProductState deliveryState = DeliveryProductState.fromStringDeliveryState(state)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order state: " + state));

        return deliveryRepository.getDeliveryState(deliveryState).stream()
                .map(this::convertDeliveryProductsDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<GetDeliveryPdtForDlvManDTO> getDeliveryForDlvMen(String state) {
        DeliveryProductState deliveryState = DeliveryProductState.fromStringDeliveryState(state)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order state: " + state));

        return deliveryRepository.getDeliveryStateDTO(deliveryState).stream()
                .map(this::convertDeliveryPdtForDeliveryMan)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryGroupedBySellerDTO> getGroupedDeliveries(String state) {

        List<GetDeliveryPdtForDlvManDTO> deliveries = getDeliveryForDlvMen(state);

        return deliveryRepository.getGroupedDeliveries().stream()
                .map(item -> new DeliveryGroupedBySellerDTO(
                        item.getSellerId(),
                        item.getSellerName(),
                        item.getStarPointSeller(),
                        item.getTotalOrders(),
                        deliveries
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void updateStateDeliverYMatch(Long id) {
        try {
            deliveryRepository.updateStateMatch(
                    id,
                    DeliveryProductState.TOMADO,
                    DeliveryProductState.EN_COLA
                    );
        } catch (Exception e){
            System.err.println(e);
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

    public GetDeliveryProductDTO convertDeliveryProductsDTO(DeliveryProduct delivery) {
        return new GetDeliveryProductDTO(
                delivery.getId(),
                delivery.getDate(),
                delivery.getAvailable(),
                delivery.getState(),
                delivery.getStartingPoint(),
                delivery.getDestiny(),
                delivery.getOrder().getId_order(),
                delivery.getDeliveryMan() != null ? delivery.getDeliveryMan().getId_deliveryMan() : null
        );
    }

    public GetDeliveryPdtForDlvManDTO convertDeliveryPdtForDeliveryMan(GetDeliveryPdtForDlvManDTO deliveryProduct) {
        Long idOrder = deliveryProduct.getIdOrder();
        Long idShoppingCart = deliveryProduct.getIdShoppingCard();
        String destination = deliveryProduct.getDestinyClient();

        Order order = orderRepository.getOrderById(idOrder);
        var orderItems = orderRepository.convertToOrder(order);

        RequestCoordinatesDTO destinations = new RequestCoordinatesDTO();
        destinations.setDestination(destination);
        int deliveryCost = distanceServiceImp.CalcularTarifa(destinations, idShoppingCart);

        return new GetDeliveryPdtForDlvManDTO(
                deliveryProduct.getIdDelivery(),
                deliveryProduct.getUserName(),
                deliveryProduct.getStartPointSeller(),
                destination,
                idOrder,
                idShoppingCart,
                orderItems.getShoppingCartId().getCartItems(),
                deliveryCost
        );
    }

}
