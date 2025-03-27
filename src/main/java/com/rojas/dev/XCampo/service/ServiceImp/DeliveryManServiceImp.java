package com.rojas.dev.XCampo.service.ServiceImp;

import com.rojas.dev.XCampo.entity.DeliveryMan;
import com.rojas.dev.XCampo.entity.Roles;
import com.rojas.dev.XCampo.entity.User;
import com.rojas.dev.XCampo.repository.DeliveryManRepository;
import com.rojas.dev.XCampo.repository.RolesRepository;
import com.rojas.dev.XCampo.repository.UserRepository;
import com.rojas.dev.XCampo.service.Interface.DeliveryManService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Service
public class DeliveryManServiceImp implements DeliveryManService {

    @Autowired
    RolesRepository rolesRepository;

    @Autowired
    DeliveryManRepository deliveryManRepository;

    @Autowired
    UserRepository userRepository;

    /**
     * agrega un nuevo repartidor
     * @param deliveryMan
     * @param idRol
     * @return estado http
     */
    @Override
    public ResponseEntity<?> insertDeliveryMan(DeliveryMan deliveryMan, Long idRol) {
        Optional<Roles> result = rolesRepository.findById(idRol);

        try {
            if (result.isEmpty()) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while get the rol: ");

            deliveryMan.setRol(result.get());
            deliveryManRepository.save(deliveryMan);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id_deliveryMan}")
                    .buildAndExpand(deliveryMan.getId_deliveryMan())
                    .toUri();

            return ResponseEntity.created(location).body(deliveryMan);

        } catch (DataIntegrityViolationException e) {
            // Error si hay violaciones de integridad en los datos (ej. campos únicos)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Data integrity violation: " + e.getMessage());
        }
    }

    /**
     * expone el id del repartidor
     * @param user_id
     * @return estado http
     */
    @Override
    public ResponseEntity<?> getIdDeliveryManByUser(Long user_id) {
        Optional<User> user = userRepository.findById(user_id);
        if (user.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id " + user_id + " not found.");

        Optional<Long> result = deliveryManRepository.getIdClientByIdUser(user.get());
        if (result.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id " + user_id + " not found.");

        return ResponseEntity.ok().body(result.get());
    }

    /**
     * actualiza los datos de un repartidor
     * @param deliveryMan
     * @return estadi http
     */
    @Override
    public ResponseEntity<?> updateDeliveryMan(DeliveryMan deliveryMan) {
        if(!deliveryManRepository.existsById(deliveryMan.getId_deliveryMan())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("deliveryMan with id " + deliveryMan.getId_deliveryMan() + " not found.");
        }

        deliveryManRepository.updateClient(
                deliveryMan.getId_deliveryMan(),
                deliveryMan.getRute());

        return ResponseEntity.status(HttpStatus.OK).body("deliveryMan update successfully.");
    }

    /**
     * busca el repartidor
     * @param deliveryMan_id
     * @return repartidor
     */
    @Override
    public ResponseEntity<?> getDeliveryManById(Long deliveryMan_id) {
        Optional<DeliveryMan> deliveryMan = deliveryManRepository.findById(deliveryMan_id);
        if(deliveryMan.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("deliveryMan with id " + deliveryMan_id + " not found.");

        return ResponseEntity.ok().body(deliveryMan.get());
    }

    /**
     * elimina un repartidor
     * @param deliveryMan_id
     * @return
     */
    @Override
    public ResponseEntity<?> delete(Long deliveryMan_id) {
        if (!deliveryManRepository.existsById(deliveryMan_id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("delivery man with id " + deliveryMan_id + " not found.");
        }

        deliveryManRepository.deleteById(deliveryMan_id);
        // Retornar código 200 (OK) indicando que se eliminó correctamente
        return ResponseEntity.status(HttpStatus.OK).body("delivery man deleted successfully.");
    }
}
