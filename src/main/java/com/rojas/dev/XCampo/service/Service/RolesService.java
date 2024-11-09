package com.rojas.dev.XCampo.service.Service;

import com.rojas.dev.XCampo.entity.Roles;
import com.rojas.dev.XCampo.entity.UserRole;

import java.util.Set;

public interface RolesService {

    Set<Roles> assignRolesToUser(Set<Long> rolesIds);
    Roles findRolesType(UserRole nameRol);
    Roles findRoleById(Long id);

}
