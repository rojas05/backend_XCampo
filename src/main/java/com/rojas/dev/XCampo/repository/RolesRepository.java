package com.rojas.dev.XCampo.repository;

import com.rojas.dev.XCampo.entity.Roles;
import com.rojas.dev.XCampo.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RolesRepository extends JpaRepository<Roles, Long> {

    @Transactional
    @Query("SELECT r.nameRole FROM Roles r WHERE r.user = :id_user")
    Optional<List<String>> getRolesByUser(@Param("id_user") Optional<User> user);

}
