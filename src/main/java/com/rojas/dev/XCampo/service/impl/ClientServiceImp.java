package com.rojas.dev.XCampo.service.impl;

import com.rojas.dev.XCampo.entity.Client;
import com.rojas.dev.XCampo.entity.Roles;
import com.rojas.dev.XCampo.entity.Seller;
import com.rojas.dev.XCampo.entity.User;
import com.rojas.dev.XCampo.repository.ClientRepository;
import com.rojas.dev.XCampo.repository.RolesRepository;
import com.rojas.dev.XCampo.repository.UserRepository;
import com.rojas.dev.XCampo.service.Interface.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Service
public class ClientServiceImp implements ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    RolesRepository rolesRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public ResponseEntity<?> insertClient(Client client, Long idRol) {
        try {
            Optional<Roles> result = rolesRepository.findById(idRol);
                if (result.isPresent()){
                    client.setRol(result.get());
                    clientRepository.save(client);
                    URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/{id_client}")
                            .buildAndExpand(client.getId_client())
                            .toUri();
                    return ResponseEntity.created(location).body(client);
                }else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Error occurred while get the rol: ");
                }

            } catch (Exception e) {
                // Cualquier otro error general
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error occurred while saving the client: " + e.getMessage());
            }
    }

    @Override
    public ResponseEntity<?> getIdClientByUser(Long user_id) {
        try {
            Optional<User> user = userRepository.findById(user_id);
            if (user.isPresent()){
                Optional<Long> result = clientRepository.getIdClientByIdUser(user.get());
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
    public ResponseEntity<?> updateClient(Client client) {
        try {
            if(clientRepository.existsById(client.getId_client())){
                clientRepository.updateClient(
                        client.getId_client(),
                        client.getLocation_description(),
                        client.getName());
                return ResponseEntity.status(HttpStatus.OK).body("Seller update successfully.");
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seller with id " + client.getId_client() + " not found.");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while update the seller: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getSellerById(Long id) {
        try {
            Optional<Client> client = clientRepository.findById(id);
            if(client.isPresent()){
                return ResponseEntity.ok().body(client);
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("client with id " + id + " not found.");
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while get the client: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> delete(Long id_client) {
        try {
            // Verificar si el vendedor existe
            if (clientRepository.existsById(id_client)) {
                // existe, eliminar el vendedor
                clientRepository.deleteById(id_client);
                // Retornar código 200 (OK) indicando que se eliminó correctamente
                return ResponseEntity.status(HttpStatus.OK).body("client deleted successfully.");
            } else {
                // Si no se encuentra el vendedor, retornar 404 (Not Found)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("client with id " + id_client + " not found.");
            }
        } catch (Exception e) {
            // En caso de cualquier otro error, retornar código 500 (Internal Server Error)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while deleting the seller: " + e.getMessage());
        }
    }
}
