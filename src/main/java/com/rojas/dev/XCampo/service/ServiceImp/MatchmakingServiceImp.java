package com.rojas.dev.XCampo.service.ServiceImp;

import com.rojas.dev.XCampo.dto.DeliveryManMatchDto;
import com.rojas.dev.XCampo.dto.DeliveryMatchDto;
import com.rojas.dev.XCampo.dto.TokenNotificationID;
import com.rojas.dev.XCampo.repository.DeliveryManRepository;
import com.rojas.dev.XCampo.repository.DeliveryRepository;
import com.rojas.dev.XCampo.service.Interface.MatchmakingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MatchmakingServiceImp implements MatchmakingService {

    @Autowired
    DeliveryRepository deliveryRepository;

    @Autowired
    DeliveryManRepository deliveryManRepository;

    /**
     * funcion principal del servicio de emparejamiento
     * @param id
     * @return lista de tokens para notificacion
     */
    @Override
    public Queue<TokenNotificationID> match(Long id) {
        Optional<String> locationDelivery = getLocationDelivery(id);

        if(locationDelivery.isEmpty()){
            System.err.println("|xx|====> Problema al encontrar la vereda");
            return null;
        }

        String location = locationDelivery.get();
        List<DeliveryMatchDto> deliveyList = getDelivery(location);
        List<DeliveryManMatchDto> deliveryManList = getDeliveryMan(location);

        if(deliveryManList == null || deliveyList == null){
            System.err.println("|xx|====> Problema con las consultas");
            return null;
        }

        return matchDeliveryManAndDelivery(deliveryManList, deliveyList);
    }

    /**
     * funcion para extrarer la localizacion del delivery que disparo el evento
     *
     * @param id
     * @return localizacion
     */
    private Optional<String> getLocationDelivery(Long id){
        try {
            return deliveryRepository.getDeliveryLocation(id)
                    .filter(location -> !location.isBlank()) // Evita devolver strings vacíos
                    .or(() -> {
                        System.err.println("|xx|====> Consulta vacía");
                        return Optional.empty();
                    });
        } catch (Exception e){
            System.err.println("|xx|====> Error en consulta de localization: " + e);
            return Optional.empty();
        }
    }

    /**
     * funcion que extrae los envios en cola
     * @param location
     * @return lista en dto con los datos requeridos
     */
    private  List<DeliveryMatchDto> getDelivery(String location){
        try {
            Optional<List<DeliveryMatchDto>> result = deliveryRepository.getLocationsDelivery(location); // filtrar por municipio
            if(result.isEmpty()){
                System.err.println("|xx|====> Consulta vacia: ");
            }
            return result.get();
        } catch (Exception e){
            System.err.println("|xx|====> Error en consulta del ENVIO =>" + e);
            return null;
        }
    }

    /**
     * funcion que extrae los repartidores
     * @param location
     * @return lista en dto con los datos requeridos
     */
    private  List<DeliveryManMatchDto> getDeliveryMan(String location){
        try {
            Optional<List<DeliveryManMatchDto>> result = deliveryManRepository.getLocationsDeliveryMan(location);
            if(result.isEmpty()){
                System.err.println("|xx|====> Consulta vacia");
            }
            return result.get();
        } catch (Exception e){
            System.err.println("|xx|====> Error en consulta al obtener el repartidor: " + e);
            return null;
        }
    }

    /**
     * funcion que recorre las listas para encontrar los repartidores adecuados
     * @param deliveryManList
     * @param deliveryList
     * @return lista de tokens de reoartidores adecuados
     */
    private Queue<TokenNotificationID> matchDeliveryManAndDelivery(List<DeliveryManMatchDto> deliveryManList, List<DeliveryMatchDto> deliveryList){
        // Se utiliza una cola para almacenar los tokens de notificación
        Queue<TokenNotificationID> tokensList = new LinkedList<>();

        // Conjunto para evitar coincidencias duplicadas entre un mismo pedido y repartidor
        Set<String> matchedPairs = new HashSet<>();

        // Mapa para asegurarnos de que un pedido solo sea asignado una vez
        Set<Long> assignedDeliveries = new HashSet<>();

        // Recorremos la lista de pedidos
        for (DeliveryMatchDto delivery : deliveryList) {
            Long deliveryIdStr = delivery.getId(); // Convertimos el ID a String para evitar inconsistencias

            // Si el pedido ya fue asignado, pasamos al siguiente
            if (assignedDeliveries.contains(deliveryIdStr)) {
                continue;
            }

            // Recorremos la lista de repartidores
            for (DeliveryManMatchDto deliveryMan : deliveryManList) {
                // Recorremos la lista de ubicaciones del repartidor directamente
                for (String location : deliveryMan.getLocationsList()) {
                    // Verificamos si la ubicación del repartidor coincide con la del pedido
                    if (location.equals(delivery.getLocation())) {
                        String key = deliveryIdStr + "-" + deliveryMan.getToken(); // Clave única para pedido-repartidor

                        if (!matchedPairs.contains(key)) { // Verificamos que no se haya agregado antes para el mismo pedido
                            System.out.println("✅ MATCH IN " + delivery.getLocation());
                            tokensList.offer(new TokenNotificationID(delivery.getId(), deliveryMan.getToken()));
                            matchedPairs.add(key); // Guardamos la coincidencia para evitar duplicados del mismo pedido
                            assignedDeliveries.add(deliveryIdStr); // Marcamos el pedido como asignado
                            break; // Salimos del bucle de ubicaciones al encontrar la primera coincidencia
                        }
                    }
                }

                // Si el pedido ya fue asignado, salimos del bucle de repartidores
                if (assignedDeliveries.contains(deliveryIdStr)) {
                    break;
                }
            }
        }

        // Si hay al menos dos coincidencias, se retorna la cola; de lo contrario, se retorna null
        return (tokensList.size() >= 2) ? tokensList : null;
    }

}
