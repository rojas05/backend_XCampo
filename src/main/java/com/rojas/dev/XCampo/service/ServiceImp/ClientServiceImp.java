package com.rojas.dev.XCampo.service.ServiceImp;

import com.rojas.dev.XCampo.entity.Client;
import com.rojas.dev.XCampo.entity.Roles;
import com.rojas.dev.XCampo.entity.User;
import com.rojas.dev.XCampo.enumClass.UserRole;
import com.rojas.dev.XCampo.exception.EntityNotFoundException;
import com.rojas.dev.XCampo.exception.InvalidDataException;
import com.rojas.dev.XCampo.repository.ClientRepository;
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
    RolesServiceImp rolesServiceImp;

    @Autowired
    UserRepository userRepository;

    @Override
    public ResponseEntity<?> insertClient(Client client, Long idRol) {
        Roles result = rolesServiceImp.findRoleById(idRol);
        if (result.getNameRole() != UserRole.CLIENT) throw new InvalidDataException("EL ID de ROL no es de CLIENT: " + idRol);

        client.setRol(result);
        clientRepository.save(client);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id_client}")
                .buildAndExpand(client.getId_client())
                .toUri();

        return ResponseEntity.created(location).body(client);
    }

    @Override
    public ResponseEntity<?> getIdClientByUser(Long user_id) {
        Optional<User> user = userRepository.findById(user_id);
        if (user.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id " + user_id + " not found.");

        Optional<Long> result = clientRepository.getIdClientByIdUser(user.get());
        if (result.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id " + user_id + " not found.");

        return ResponseEntity.ok().body(result);
    }

    @Override
    public ResponseEntity<?> updateClient(Client client) {
        existsClient(client.getId_client());

        clientRepository.updateClient(
                client.getId_client(),
                client.getLocation_description(),
                client.getName());

        return ResponseEntity.status(HttpStatus.OK).body("Client update successfully.");
    }

    @Override
    public ResponseEntity<?> getSellerById(Long id) {
        Optional<Client> client = clientRepository.findById(id);
        if(client.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("client with id " + id + " not found.");
        }

        return ResponseEntity.ok().body(client);
    }

    @Override
    public ResponseEntity<?> delete(Long id_client) {
        existsClient(id_client);
        clientRepository.deleteById(id_client);

        return ResponseEntity.status(HttpStatus.OK).body("client deleted successfully.");
    }

    public Client findClientById(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client ID NOT found: " + clientId));
    }

    public void existsClient(Long idClient) {
        if (!clientRepository.existsById(idClient)) {
            throw new IllegalStateException("client with id " + idClient + " not found.");
        }
    }

}
