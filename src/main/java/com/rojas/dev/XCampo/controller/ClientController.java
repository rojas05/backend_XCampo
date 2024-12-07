package com.rojas.dev.XCampo.controller;

import com.rojas.dev.XCampo.entity.Client;
import com.rojas.dev.XCampo.service.Interface.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
public class ClientController {
    @Autowired
    ClientService clientService;

    @PostMapping("{idRol}")
    ResponseEntity<?> insertClient(@RequestBody Client client,@PathVariable Long idRol){
        return clientService.insertClient(client, idRol);
    }
    @DeleteMapping("{id_client}")
    public ResponseEntity<?> deleteSeller(@PathVariable Long id_client){
        return clientService.delete(id_client);
    }

    @GetMapping("idUser/{id_user}")
    public ResponseEntity<?> getIdSellerByIdUser(@PathVariable Long id_user){
        return clientService.getIdClientByUser(id_user);
    }

    @GetMapping("{id_client}")
    public ResponseEntity<?> getSellerById(@PathVariable Long id_client){
        return clientService.getSellerById(id_client);
    }

    @PatchMapping
    public ResponseEntity<?> updateSeller(@RequestBody Client client){
        return clientService.updateClient(client);
    }

}
