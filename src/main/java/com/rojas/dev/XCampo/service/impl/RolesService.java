package com.rojas.dev.XCampo.service.impl;

import com.rojas.dev.XCampo.entity.Roles;
import com.rojas.dev.XCampo.entity.User;
import com.rojas.dev.XCampo.repository.RolesRepository;
import com.rojas.dev.XCampo.service.Interface.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolesService implements RoleService {

    @Autowired
    RolesRepository rolesRepository;

    @Autowired
    UserService userService;


    @Override
    public ResponseEntity<?> insertNewRolUser(Roles role, Long user) {
        try {
            User result = userService.findByIdUser(user);
            role.setUser(result);
            rolesRepository.save(role);
           return ResponseEntity.ok(role);
        } catch (Exception e){
            return ResponseEntity.ok().body("Seller created no successfully." + e.getMessage());
        }

    }

    @Override
    public ResponseEntity<?> getRolesByIdUser(Long idUser) {
        try {
            User user = userService.findByIdUser(idUser);
            Optional<List<String>> result = rolesRepository.getRolesByUser(user);
            if (result.isPresent()){
                return ResponseEntity.ok().body(result);
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user with id " + userService + " not found.");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while get the user: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getRolesById(Long id) {
        try {
            Optional<Roles> role = rolesRepository.findById(id);
            if(role.isPresent()){
                return ResponseEntity.ok().body(role);
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seller with id " + id + " not found.");
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while get the seller: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> insertRoles(Roles rol) {
        try {
            if(rol.getSeller() != null){
                rol.getSeller().setRol(rol);
            }
            rolesRepository.save(rol);
            return ResponseEntity.ok().body(rol);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while get the rol: " + e.getMessage());
        }
    }
}
