package com.rojas.dev.XCampo.service.ServiceImp;

import com.rojas.dev.XCampo.entity.Roles;
import com.rojas.dev.XCampo.entity.User;
import com.rojas.dev.XCampo.entity.UserRole;
import com.rojas.dev.XCampo.repository.RolesRepository;
import com.rojas.dev.XCampo.service.Interface.RolesService;
import com.rojas.dev.XCampo.service.Interface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.List;

@Service
public class RolesServiceImp implements RolesService {

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    UserService userService;

    @Override
    public ResponseEntity<?> insertNewRolUser(Roles role, Long user) {
        try {
            Optional<User> result = userService.findByIdUser(user);
            role.setUser(result.orElse(null));
            rolesRepository.save(role);
            return ResponseEntity.ok(role);
        } catch (Exception e){
            return ResponseEntity.ok().body("Seller created no successfully." + e.getMessage());
        }

    }

    @Override
    public ResponseEntity<?> getRolesByIdUser(Long idUser) {
        try {
            Optional<User> user = userService.findByIdUser(idUser);
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

    @Transactional
    @Override
    public Set<Roles> assignRolesToUser(Set<Long> rolesIds) {
        Set<Roles> rolesUser = new HashSet<>();

        for (Long rolesId : rolesIds) {
            Roles rolUser = findRoleById(rolesId);
            rolesUser.add(rolUser);
        }

        return rolesUser;
    }

    @Override
    public Roles findRolesType(UserRole nameRol) {
        return null;
    }

    @Override
    public Roles findRoleById(Long id) {
        return rolesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol not found"));
    }

}
