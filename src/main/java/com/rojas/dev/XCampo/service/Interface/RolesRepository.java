package com.rojas.dev.XCampo.service.Interface;

import com.rojas.dev.XCampo.entity.Roles;
import com.rojas.dev.XCampo.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<Roles, Long> {

    public Optional<Roles> findByRoleType(UserRole nameRole);

}
