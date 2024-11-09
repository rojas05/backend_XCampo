package com.rojas.dev.XCampo.repository;

import com.rojas.dev.XCampo.entity.Roles;
import com.rojas.dev.XCampo.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<Roles, Long> {

    //

}
