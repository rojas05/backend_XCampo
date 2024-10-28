package com.rojas.dev.XCampo.service.impl;

import com.rojas.dev.XCampo.entity.Roles;
import com.rojas.dev.XCampo.entity.UserRole;
import com.rojas.dev.XCampo.repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class RolesService {

    @Autowired
    private RolesRepository rolesRepository;

    @Transactional
    public Set<Roles> assignRolesToUser(Set<Long> rolesIds) {
        Set<Roles> rolesUser = new HashSet<>();

        for (Long rolesId : rolesIds) {
            Roles rolUser = findRoleById(rolesId);
            rolesUser.add(rolUser);
        }

        return rolesUser;
    }

    public Roles findRolesType(UserRole nameRol) {
        return null;
    }

    public Roles findRoleById(Long id) {
        return rolesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol not found"));
    }

}
