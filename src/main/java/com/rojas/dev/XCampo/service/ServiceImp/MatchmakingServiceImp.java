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
    private Queue<TokenNotificationID> matchDeliveryManAndDelivery(List<DeliveryManMatchDto> deliveryManList, List<DeliveryMatchDto> deliveryList) {
        System.out.println("repartidores= " + deliveryManList );
        System.out.println("domicilios= " + deliveryList );
        Queue<TokenNotificationID> tokensList = new LinkedList<>();
        Set<String> matchedPairs = new HashSet<>();

        // Recorremos la lista de repartidores
        for (DeliveryManMatchDto deliveryMan : deliveryManList) {
            List<Long> matchDeliveryList = new ArrayList<>();

            // Recorremos la lista de pedidos
            for (DeliveryMatchDto delivery : deliveryList) {
                String key = delivery.getId() + "-" + deliveryMan.getToken();

                // Evita duplicados
                if (matchedPairs.contains(key)) {
                    continue; // Si ya procesamos esta combinación, pasamos al siguiente pedido
                }

                // Recorremos las ubicaciones del repartidor
                for (String location : deliveryMan.getLocationsList()) {
                    if (location.equals(delivery.getLocation())) {
                        System.out.println("✅ MATCH IN " + delivery.getLocation());
                        matchDeliveryList.add(delivery.getId());
                        matchedPairs.add(key); // Se guarda para evitar duplicados
                        break; // Salimos del bucle de ubicaciones porque ya hicimos match
                    }
                }
            }

            // Si un repartidor coincidió con al menos 2 pedidos, lo agregamos a la cola
            if (matchDeliveryList.size() >= 2) {
                tokensList.offer(new TokenNotificationID(matchDeliveryList, deliveryMan.getToken()));
            }
        }

        return tokensList;
    }

}
