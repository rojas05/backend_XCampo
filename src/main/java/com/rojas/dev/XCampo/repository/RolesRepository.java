package com.rojas.dev.XCampo.repository;

import com.rojas.dev.XCampo.entity.Roles;
import com.rojas.dev.XCampo.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long> {

    /**
     * consulta de roles
     * @param user
     * @return lista de roles
     */
    @Transactional
    @Query("SELECT r.nameRole FROM Roles r WHERE r.user = :id_user")
    Optional<List<String>> getRolesByUser(@Param("id_user") User user);

}
