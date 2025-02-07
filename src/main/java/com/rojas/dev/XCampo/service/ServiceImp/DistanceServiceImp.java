package com.rojas.dev.XCampo.service.ServiceImp;


import com.rojas.dev.XCampo.dto.RequestCoordinatesDTO;
import com.rojas.dev.XCampo.repository.CartItemRepository;
import com.rojas.dev.XCampo.repository.ShoppingCartRepository;
import com.rojas.dev.XCampo.service.Interface.DistanceService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DistanceServiceImp implements DistanceService {

    @Autowired
    private CartItemRepository shoppingCartRepository;

    RestTemplate restTemplate = new RestTemplate();

    @Value("${maps.url}")
    private String GOOGLE_MAPS_API_URL;

    @Value("${maps.api_key}")
    private String API_KEY;

    @Override
    public ResponseEntity<?> CalcularTarifa(RequestCoordinatesDTO request, Long idCart) {
        try {
            Optional<List<String>> result = shoppingCartRepository.findStoreCoordinatesByCartId(idCart);
            if(result.isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id cart failed" + result);
            System.out.println(result.get());
            String origen = getDestini(result.get());
            String waypoints = getWayPoints(result.get());

            double distanceKm = getDistanceKm(request,origen,waypoints);
            if(distanceKm == 0.0)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("api maps failed");
            Double tarifa = (distanceKm <= 2.5) ? 1000 : distanceKm * 400;
            return ResponseEntity.ok(redondear(tarifa.intValue()));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    private Double getDistanceKm(RequestCoordinatesDTO destination, String origin, String waypoints){
        try {

            String url;
            if (waypoints == null){
                url = UriComponentsBuilder.fromHttpUrl(GOOGLE_MAPS_API_URL)
                        .queryParam("origin", origin)
                        .queryParam("destination", destination.getDestination())
                        .queryParam("key", API_KEY)
                        .build()
                        .toString();
            }else {
                 url = UriComponentsBuilder.fromHttpUrl(GOOGLE_MAPS_API_URL)
                        .queryParam("origin", destination.getDestination())
                        .queryParam("destination", destination.getDestination())
                        .queryParam("waypoints", waypoints)
                        .queryParam("key", API_KEY)
                        .build()
                        .toString();
            }

            System.out.println(url);

            String response = restTemplate.getForObject(url, String.class);

            JSONObject json = new JSONObject(response);
            if(!json.getJSONArray("routes").isEmpty()){
                JSONObject route = json.getJSONArray("routes").getJSONObject(0);
                JSONObject leg = route.getJSONArray("legs").getJSONObject(0);
                int distanceMeters = leg.getJSONObject("distance").getInt("value");

                return distanceMeters / 1000.0;
            }

            return 0.0;
        } catch (Exception e){
            return 0.0;
        }
    }

    private static int redondear(Integer numero) {
        int residuo = numero % 100;
        if (residuo >= 50) {
            return numero + (100 - residuo); // Redondea hacia arriba
        } else {
            return numero - residuo; // Redondea hacia abajo
        }
    }

    private static String getDestini(List<String> coordinates){
        return coordinates.get(0);
    }

    private static String getWayPoints(List<String> coordinates){
        if (coordinates == null || coordinates.size() <= 1) {
            return null; // Retorna vacío si no hay suficientes coordenadas
        }

        return coordinates.stream()
                .skip(1) // Omitir la primera coordenada
                .collect(Collectors.joining("|")); // Unir con '|'
    }
}
