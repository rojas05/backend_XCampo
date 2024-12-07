package com.rojas.dev.XCampo.service.ServiceImp;

import com.rojas.dev.XCampo.entity.Roles;
import com.rojas.dev.XCampo.entity.User;
import com.rojas.dev.XCampo.enumClass.UserRole;
import com.rojas.dev.XCampo.exception.EntityNotFoundException;
import com.rojas.dev.XCampo.repository.RolesRepository;
import com.rojas.dev.XCampo.service.Interface.RolesService;
import com.rojas.dev.XCampo.service.Interface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RolesServiceImp implements RolesService {

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity<?> insertNewRolUser(Roles role, Long user) {
        Optional<User> result = userService.findByIdUser(user);
        role.setUser(result.orElse(null));
        rolesRepository.save(role);
        return ResponseEntity.ok(role);
    }

    @Override
    public ResponseEntity<?> getRolesByIdUser(Long idUser) {
        User user = userService.findByIdUser(idUser)
                .orElseThrow(() -> new EntityNotFoundException("User not found for the specified ID: " + idUser));
        Optional<List<String>> result = rolesRepository.getRolesByUser(user);

        if (result.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user with id " + userService + " not found.");

        return ResponseEntity.ok().body(result);
    }

    @Override
    public ResponseEntity<?> getRolesById(Long id) {
        Optional<Roles> role = rolesRepository.findById(id);
        if(role.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seller with id " + id + " not found.");

        return ResponseEntity.ok().body(role);
    }

    @Override
    public ResponseEntity<?> insertRoles(Roles rol) {
        if(rol.getSeller() != null){
            rol.getSeller().setRol(rol);
        }
        rolesRepository.save(rol);
        return ResponseEntity.ok().body(rol);
    }

    @Transactional
    @Override
    public Set<Roles> assignRolesToUser(Set<Long> rolesIds) {
        return rolesIds.stream()
                .map(this::findRoleById)
                .collect(Collectors.toSet());
    }

    @Override
    public Roles findRolesType(UserRole nameRol) {
        return null;
    }

    @Override
    public Roles findRoleById(Long idRol) {
        return rolesRepository.findById(idRol)
                .orElseThrow(() -> new EntityNotFoundException("Error occurred while get the rol: " + idRol));
    }

}
