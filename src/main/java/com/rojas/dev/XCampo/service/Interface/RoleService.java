package com.rojas.dev.XCampo.service.Interface;

import com.rojas.dev.XCampo.entity.Roles;
import org.springframework.http.ResponseEntity;

import javax.management.relation.Role;
import java.util.Optional;

public interface RoleService {

    ResponseEntity<?> insertNewRolUser(Roles role, Long user);

    ResponseEntity<?> getRolesByIdUser(Long idUser);

    ResponseEntity<?> getRolesById(Long id);

    ResponseEntity<?> insertRoles(Roles rol);

}
