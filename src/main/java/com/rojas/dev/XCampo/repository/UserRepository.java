package com.rojas.dev.XCampo.repository;

import com.rojas.dev.XCampo.entity.User;
import com.rojas.dev.XCampo.enumClass.UserRole;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String mail);

    /**
     * consulta de usuario
     * @param id_user
     * @return
     */
    @Transactional
    @Query("SELECT u.nfs FROM User u WHERE u.user_id = :id_user")
    Optional<String> getNFSidByIdUser(@Param("id_user") Long id_user);

    /**
     * Actualizar infomacion de usuario
     * @param idUser
     * @param nfs
     */
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.nfs = :nfs WHERE u.user_id = :idUser")
    void updateNfs(@Param("idUser") Long idUser,
                          @Param("nfs") String nfs);

    /**
     * consulta de roles
     * @param role
     * @return lista de roles
     */
    @Transactional
    @Query("SELECT u.nfs FROM User u " +
            "JOIN u.roles r WHERE r.nameRole = :role")
    List<String> findFcmTokensByRole(UserRole role);

    @Transactional
    @Query("SELECT u.nfs FROM User u " +
            "JOIN u.roles r " +
            "JOIN r.client c " +
            "WHERE c.id_client = :id")
    String findFcmTokensByIdClient(Long id);

    @Transactional
    @Query("SELECT u.nfs FROM User u " +
            "JOIN u.roles r " +
            "JOIN r.seller s " +
            "WHERE s.id_seller = :id")
    String findFcmTokensByIdSeller(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.name = :name, u.department = :department, u.city = :city, u.cell = :cell, u.email = :email WHERE u.id = :userId")
    int updateUser(
            @Param("userId") Long userId,
            @Param("name") String name,
            @Param("department") String department,
            @Param("city") String city,
            @Param("cell") Long cell,
            @Param("email") String email
    );
}
