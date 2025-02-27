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

    @Transactional
    @Query("SELECT u.nfs FROM User u WHERE u.user_id = :id_user")
    Optional<String> getNFSidByIdUser(@Param("id_user") Long id_user);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.nfs = :nfs WHERE u.user_id = :idUser")
    void updateNfs(@Param("idUser") Long idUser,
                          @Param("nfs") String nfs);

    @Transactional
    @Query("SELECT u.nfs FROM User u " +
            "JOIN u.roles r WHERE r.nameRole = :role")
    List<String> findFcmTokensByRole(UserRole role);

}
