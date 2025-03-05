package com.rojas.dev.XCampo.service.ServiceImp;

import com.rojas.dev.XCampo.dto.DeliveryMamMatchDto;
import com.rojas.dev.XCampo.dto.DeliveryMatchDto;
import com.rojas.dev.XCampo.repository.DeliveryManRepository;
import com.rojas.dev.XCampo.repository.DeliveryRepository;
import com.rojas.dev.XCampo.service.Interface.MatchmakingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public List<String> match(Long id) {
        String location = getLocationDelivery(id);

        if(location == null){
            System.err.println("|xx|====> Problema al encontrar la vereda");
            return null;
        }

        List<DeliveryMatchDto> deliveyList = getDelivery(location);

        List<DeliveryMamMatchDto> deliveryManList = getDeliveryMan(location);

        if(deliveyList == null && deliveryManList == null){
            System.err.println("|xx|====> Problema con las consultas");
            return null;
        }

        return matchDeliveryManAndDelivery(deliveryManList,deliveyList);
    }

    /**
     * funcion para extrarer la localizacion del delivery que disparo el evento
     * @param id
     * @return localizacion
     */
    private String getLocationDelivery(Long id){
        try {
            Optional<String> result = deliveryRepository.getDeliveryLocation(id);
            if(result.isEmpty()){
                System.err.println("|xx|====> Consulta vacia");
                return null;
            }
            return result.get();
        } catch (Exception e){
            System.err.println("|xx|====> Error en consulta " + e);
            return null;
        }
    }

    /**
     * funcion que extrae los envios en cola
     * @param location
     * @return lista en dto con los datos requeridos
     */
    private  List<DeliveryMatchDto> getDelivery(String location){
        try {
            Optional<List<DeliveryMatchDto>> result = deliveryRepository.getLocationsDelivery(location);
            if(result.isEmpty()){
                System.err.println("|xx|====> Consulta vacia");
            }
            return result.get();
        } catch (Exception e){
            System.err.println("|xx|====> Error en consulta " + e);
            return null;
        }
    }

    /**
     * funcion que extrae los repartidores
     * @param location
     * @return lista en dto con los datos requeridos
     */
    private  List<DeliveryMamMatchDto> getDeliveryMan(String location){
        try {
            Optional<List<DeliveryMamMatchDto>> result = deliveryManRepository.getLocationsDeliveryMan(location);
            if(result.isEmpty()){
                System.err.println("|xx|====> Consulta vacia");
            }
            return result.get();
        } catch (Exception e){
            System.err.println("|xx|====> Error en consulta " + e);
            return null;
        }
    }

    /**
     * funcion que recorre las listas para encontrar los repartidores adecuados
     * @param deliveryManList
     * @param deliveryList
     * @return lista de tokens de reoartidores adecuados
     */
    private List<String> matchDeliveryManAndDelivery(List<DeliveryMamMatchDto> deliveryManList, List<DeliveryMatchDto> deliveryList){
        List<String> tokens = new java.util.ArrayList<>(List.of());
        for (DeliveryMatchDto delivery:deliveryList){
             for(DeliveryMamMatchDto deliveryMan:deliveryManList){
                 List<String> locations = deliveryMan.getLocationsList();
                 int indice = locations.indexOf(delivery.getLocation());
                 if(indice != -1){
                     tokens.add(deliveryMan.getToken());
                     System.out.println("MATCH IN "+ locations.get(indice));
                 }else{
                     System.out.println("NO MATCH");
                 }
             }
         }
         return tokens;
    }

}
