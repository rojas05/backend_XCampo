package com.rojas.dev.XCampo.service.ServiceImp;


import com.rojas.dev.XCampo.dto.RequestCoordinatesDTO;
import com.rojas.dev.XCampo.exception.EntityNotFoundException;
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

    /**
     * calcula la tarifa a pagar por un envio
     * esta funcion realiza un peticion a google maps
     * @param request
     * @param idCart
     * @return
     */
    @Override
    public int CalcularTarifa(RequestCoordinatesDTO request, Long idCart) {
        List<String> storeCoordinates = shoppingCartRepository.findStoreCoordinatesByCartId(idCart)
                .orElseThrow(() -> new EntityNotFoundException("id cart failed" + idCart));

        System.out.println(storeCoordinates);
        String origen = getDestini(storeCoordinates);
        String waypoints = getWayPoints(storeCoordinates);

        double distanceKm = getDistanceKm(request, origen, waypoints);
        if (distanceKm == 0.0)
            return 1000;
            //throw new IllegalStateException("Error en la API de mapas: distancia calculada es 0");

        double tarifa = (distanceKm <= 2.5) ? 1000 : distanceKm * 400;
        return redondear((int) tarifa);
    }

    /**
     * funcion para optener la distancia en kilometros de la ruta
     * @param destination
     * @param origin
     * @param waypoints
     * @return
     */
    private Double getDistanceKm(RequestCoordinatesDTO destination, String origin, String waypoints) {
        try {
            String url;
            var destiny = destination.getDestination().replace("\"", "").replace(" ", "").trim();
            if (waypoints == null) {
                url = UriComponentsBuilder.fromHttpUrl(GOOGLE_MAPS_API_URL)
                        .queryParam("origin", origin)
                        .queryParam("destination", destiny)
                        .queryParam("key", API_KEY)
                        .queryParam("mode", "driving")
                        .build()
                        .toString();
            } else {
                url = UriComponentsBuilder.fromHttpUrl(GOOGLE_MAPS_API_URL)
                        .queryParam("origin", origin) // se cambio, antes (destiny)
                        .queryParam("destination", destiny)
                        .queryParam("waypoints", waypoints)
                        .queryParam("key", API_KEY)
                        .queryParam("mode", "driving")
                        .build()
                        .toString();
            }

            System.out.println(url.replace(" ", ""));

            String response = restTemplate.getForObject(url.replace(" ", ""), String.class);
            JSONObject json = new JSONObject(response);

            if (!json.getJSONArray("routes").isEmpty()) {
                JSONObject route = json.getJSONArray("routes").getJSONObject(0);
                JSONObject leg = route.getJSONArray("legs").getJSONObject(0);
                int distanceMeters = leg.getJSONObject("distance").getInt("value");

                return distanceMeters / 1000.0;
            }

            return 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }

    /**
     * con esta funcion evitamos retornar costos no validos
     * @param numero
     * @return numero
     */
    private static int redondear(Integer numero) {
        int residuo = numero % 100;
        if (residuo >= 50) {
            return numero + (100 - residuo); // Redondea hacia arriba
        } else {
            return numero - residuo; // Redondea hacia abajo
        }
    }

    /**
     * extrae el destino del request
     * @param coordinates
     * @return Destini
     */
    private static String getDestini(List<String> coordinates) {
        return coordinates.get(0).replace("\"", "").trim();
    }

    /**
     * extrae los puntos de parada y los convierte en una cadena lejible para el api de google maps
     * @param coordinates
     * @return WayPoints
     */
    private static String getWayPoints(List<String> coordinates) {
        if (coordinates == null || coordinates.size() <= 1) {
            return null; // Retorna vacío si no hay suficientes coordenadas
        }

        return coordinates.stream()
                .skip(1) // Omitir la primera coordenada
                .map(coord -> coord.replace("\"", "").trim()) // Eliminar comillas y espacios extra
                .collect(Collectors.joining("|")); // Unir con '|'
    }
}
