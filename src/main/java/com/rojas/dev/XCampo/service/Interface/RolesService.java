package com.rojas.dev.XCampo.service.Interface;

import com.rojas.dev.XCampo.entity.Roles;
import com.rojas.dev.XCampo.enumClass.UserRole;
import org.springframework.http.ResponseEntity;

import java.util.Set;

public interface RolesService {

    ResponseEntity<?> insertNewRolUser(Roles role, Long user);

    ResponseEntity<?> getRolesByIdUser(Long idUser);

    ResponseEntity<?> getRolesById(Long id);

    ResponseEntity<?> insertRoles(Roles rol);

    Set<Roles> assignRolesToUser(Set<Long> rolesIds);

    Roles findRolesType(UserRole nameRol);

    Roles findRoleById(Long id);

}
