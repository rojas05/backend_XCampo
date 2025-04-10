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
import java.util.Collections;
import java.util.List;
import java.util.Map;
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

    /**
     * iniciando kafka
     * @param kafkaTemplate
     */
    @Autowired
    public void init(KafkaTemplate<String,String> kafkaTemplate){
        DeliveryServiceImp.kafkaTemplate = kafkaTemplate;
    }

    /**
     * crear un nuevo envio
     * @param delivery
     * @return estado http
     */
    @Override
    public ResponseEntity<?> insertDelivery(GetDeliveryProductDTO delivery) {
        try {
            var idOrder = delivery.getOrderId();
            var order = orderRepository.getOrderById(idOrder);

            var idShoppingCart = order.getShoppingCart().getId_cart();
            delivery.setDestiny(orderRepository.getDestinyClient(order.getId_order()));

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

    /**
     * actualizar el estado del delivery
     * @param delivery
     * @return estado http
     */
    @Override
    public ResponseEntity<?> updateStateDelivery(DeliveryProduct delivery) {
            deliveryRepository.updateState(delivery.getId(), delivery.getState());
        onDeliveryUpdate(delivery.getId());
        return ResponseEntity.ok().body("delivery update");
    }

    /**
     * actualizar el repartidor de un envio
     * @param delivery
     * @return estado http
     */
    @Override
    public ResponseEntity<?> updateDeliveryMan(DeliveryProduct delivery) {
        deliveryRepository.updateDeliveryMan(delivery.getId(), delivery.getDeliveryMan());

        return ResponseEntity.ok().body("delivery update");
    }

    /**
     * buscar envios por cliente y estado
     * @param request
     * @return estado http
     */
    @Override
    public ResponseEntity<?> getDeliveryByClientAndState(DeliveryClientDTO request) {
        System.out.println(request);
        Optional<List<DeliveryProduct>> result  = deliveryRepository.getDeliveryByClientAndState(request.getClient(),request.getState());

        if(result.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("no dates");
        }

        return ResponseEntity.ok().body(result.get());
    }

    /**
     * guscar envios por repartidor y estado
     * @param request
     * @return estadi http
     */
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

    /**
     * buscar un envio por id
     * @param id_delivery
     * @return estado http
     */
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

    /**
     * busca los envios por la orden
     * @param id_order
     * @return dto
     */
    @Override
    public GetDeliveryPdtForDlvManDTO getDeliveryByIdOrder(Long id_order) {
        GetDeliveryPdtForDlvManDTO getOrder = deliveryRepository.getDeliveryOrderIdDTO(id_order);
        return convertDeliveryPdtForDeliveryMan(getOrder);
    }

    /**
     * busca los envios por la ruta
     * @param request
     * @return estadi http
     */
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

    /**
     * cuenta los emvios disponibles
     * @param state
     * @return cantidad
     */
    @Override
    public Long countDeliveryAvailable(String state, String municipio) {
        DeliveryProductState deliveryState = DeliveryProductState.fromStringDeliveryState(state)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order state: " + state));

        String cleanedMunicipio = municipio.toLowerCase().replace(" ", "");

        return deliveryRepository.countDeliveryAvailable(deliveryState, cleanedMunicipio);
    }

    /**
     * busca todos los envios disponibles
     * @param state
     * @return lista dto
     */
    @Override
    public List<GetDeliveryProductDTO> getAllDeliveryAvailable(String state) {
        DeliveryProductState deliveryState = DeliveryProductState.fromStringDeliveryState(state)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order state: " + state));

        return deliveryRepository.getDeliveryState(deliveryState).stream()
                .map(this::convertDeliveryProductsDTO)
                .collect(Collectors.toList());
    }

    /**
     * busca los envios por el repartidor
     * @param state
     * @return lista dto
     */
    @Override
    public List<GetDeliveryPdtForDlvManDTO> getDeliveryForDlvMen(String state, String municipio) {
        DeliveryProductState deliveryState = DeliveryProductState.fromStringDeliveryState(state)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order state: " + state));

        String cleanedMunicipio = municipio.toLowerCase().replace(" ", "");

        return deliveryRepository.getDeliveryStateDTO(deliveryState, cleanedMunicipio).stream()
                .map(this::convertDeliveryPdtForDeliveryMan)
                .collect(Collectors.toList());
    }

    public List<GetDeliveryPdtForDlvManDTO> getDeliveryForDlvMenStateDepartmanet(String state, String departament, List<String> municipio) {
        DeliveryProductState deliveryState = DeliveryProductState.fromStringDeliveryState(state)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order state: " + state));

        String cleanedDepartament = departament.toLowerCase().replace(" ", "");

        List<String> cleanedMunicipios = municipio.stream()
                .map(m -> m.toLowerCase().replace(" ", ""))
                .toList();

        return deliveryRepository.getDeliveryStateDepartamentDTO(deliveryState, cleanedDepartament, cleanedMunicipios).stream()
                .map(this::convertDeliveryPdtForDeliveryMan)
                .collect(Collectors.toList());
    }

    /**
     * agrupa los envios por repartidor
     * @param state
     * @return lista dto
     */
    @Override
    public List<DeliveryGroupedBySellerDTO> getGroupedDeliveries(String state, String departament, List<String> municipio) {
        List<GetDeliveryPdtForDlvManDTO> deliveries = getDeliveryForDlvMenStateDepartmanet(state, departament, municipio);

        // Agrupar las entregas por storageName (nombre del vendedor)
        Map<String, List<GetDeliveryPdtForDlvManDTO>> deliveriesBySeller = deliveries.stream()
                .collect(Collectors.groupingBy(dlv -> dlv.getStorageName().toLowerCase()));

        if (deliveriesBySeller.isEmpty()) {
            return Collections.emptyList();
        }

        return deliveryRepository.getGroupedDeliveries().stream()
                .map(item -> new DeliveryGroupedBySellerDTO(
                        item.getSellerId(),
                        item.getSellerName(),
                        item.getStarPointSeller(),
                        item.getTotalOrders(),
                        deliveriesBySeller.getOrDefault(item.getSellerName().toLowerCase(), Collections.emptyList())
                ))
                .collect(Collectors.toList());
    }


    /**
     * actualiza el esatdo para el servicio de match
     * @param id
     */
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

    /**
     * dispara el evento a kafka
     * @param deliveryId
     */
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

    /**
     * convierte los datos a un dto
     * @param delivery
     * @return dto
     */
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

    /**
     * convierte los datos a un dto
     * @param deliveryProduct
     * @return dto
     */
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
                deliveryProduct.getStorageName(),
                deliveryProduct.getStartPointSeller(),
                destination,
                idOrder,
                idShoppingCart,
                orderItems.getShoppingCartId().getCartItems(),
                deliveryCost
        );
    }

}
