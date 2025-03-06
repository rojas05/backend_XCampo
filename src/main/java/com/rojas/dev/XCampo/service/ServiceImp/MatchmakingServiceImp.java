package com.rojas.dev.XCampo.service.ServiceImp;

import com.rojas.dev.XCampo.dto.DeliveryManMatchDto;
import com.rojas.dev.XCampo.dto.DeliveryMatchDto;
import com.rojas.dev.XCampo.dto.TokenNotificationID;
import com.rojas.dev.XCampo.repository.DeliveryManRepository;
import com.rojas.dev.XCampo.repository.DeliveryRepository;
import com.rojas.dev.XCampo.service.Interface.MatchmakingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

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
        Queue<TokenNotificationID> tokensList = new LinkedList<>();

        for (DeliveryMatchDto delivery:deliveryList){
             for(DeliveryManMatchDto deliveryMan:deliveryManList){
                 List<String> locations = deliveryMan.getLocationsList();
                 int indice = locations.indexOf(delivery.getLocation());

                 if(indice != -1){
                     tokensList.offer(new TokenNotificationID(delivery.getId(), deliveryMan.getToken()));
                     System.out.println("✅ MATCH IN "+ locations.get(indice));
                 }else{
                     System.out.println("NO MATCH");
                 }
             }
         }

        return (tokensList.size() >= 2) ? tokensList : null;
    }

}
