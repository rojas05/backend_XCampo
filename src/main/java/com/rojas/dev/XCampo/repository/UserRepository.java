package com.rojas.dev.XCampo.repository;

import com.rojas.dev.XCampo.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String mail);

    @Transactional
    @Query("SELECT u.NFSid FROM User u WHERE u.user_id = :id_user")
    Optional<String> getNFSidByIdUser(@Param("id_user") Long id_user);

}
