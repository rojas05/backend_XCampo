package com.rojas.dev.XCampo.service.Interface;

import com.rojas.dev.XCampo.entity.Client;
import org.springframework.http.ResponseEntity;

public interface ClientService {

    ResponseEntity<?> insertClient(Client client, Long idRol);

    ResponseEntity<?> getIdClientByUser(Long user);

    ResponseEntity<?> updateClient(Client client);

    ResponseEntity<?> getSellerById(Long id);

    ResponseEntity<?> delete(Long id_client);
}
