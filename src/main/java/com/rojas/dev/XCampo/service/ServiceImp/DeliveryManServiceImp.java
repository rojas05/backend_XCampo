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


    @Override
    public ResponseEntity<?> insertDeliveryMan(DeliveryMan deliveryMan, Long idRol) {

            Optional<Roles> result = rolesRepository.findById(idRol);
            try {
                if (result.isPresent()){
                    deliveryMan.setRol(result.get());
                    deliveryManRepository.save(deliveryMan);
                    URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/{id_deliveryMan}")
                            .buildAndExpand(deliveryMan.getId_deliveryMan())
                            .toUri();
                    return ResponseEntity.created(location).body(deliveryMan);
                }else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Error occurred while get the rol: ");
                }
            } catch (DataIntegrityViolationException e) {
                // Error si hay violaciones de integridad en los datos (ej. campos únicos)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Data integrity violation: " + e.getMessage());
            } catch (Exception e) {
                // Cualquier otro error general
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error occurred while saving the seller: " + e.getMessage());
            }
    }

    @Override
    public ResponseEntity<?> getIdDeliveryManByUser(Long user_id) {
        try {
            Optional<User> user = userRepository.findById(user_id);
            if (user.isPresent()){
                Optional<Long> result = deliveryManRepository.getIdClientByIdUser(user.get());
                if (result.isPresent()){
                    return ResponseEntity.ok().body(result);
                }else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id " + user_id + " not found.");
                }
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id " + user_id + " not found.");
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while get the id seller by id user: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> updateDeliveryMan(DeliveryMan deliveryMan) {
        try {
            if(deliveryManRepository.existsById(deliveryMan.getId_deliveryMan())){
                deliveryManRepository.updateClient(
                        deliveryMan.getId_deliveryMan(),
                        deliveryMan.getRute());
                return ResponseEntity.status(HttpStatus.OK).body("deliveryMan update successfully.");
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("deliveryMan with id " + deliveryMan.getId_deliveryMan() + " not found.");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while update the deliveryMan: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getDeliveryManById(Long deliveryMan_id) {
        try {
            Optional<DeliveryMan> deliveryMan = deliveryManRepository.findById(deliveryMan_id);
            if(deliveryMan.isPresent()){
                return ResponseEntity.ok().body(deliveryMan);
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("deliveryMan with id " + deliveryMan_id + " not found.");
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while get the deliveryMan: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> delete(Long deliveryMan_id) {
        try {
            // Verificar si el vendedor existe
            if (deliveryManRepository.existsById(deliveryMan_id)) {
                // existe, eliminar el vendedor
                deliveryManRepository.deleteById(deliveryMan_id);
                // Retornar código 200 (OK) indicando que se eliminó correctamente
                return ResponseEntity.status(HttpStatus.OK).body("delivery man deleted successfully.");
            } else {
                // Si no se encuentra el vendedor, retornar 404 (Not Found)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("delivery man with id " + deliveryMan_id + " not found.");
            }
        } catch (Exception e) {
            // En caso de cualquier otro error, retornar código 500 (Internal Server Error)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while deleting the delivery man: " + e.getMessage());
        }
    }
}
