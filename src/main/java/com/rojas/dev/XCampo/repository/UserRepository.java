package com.rojas.dev.XCampo.repository;

import com.rojas.dev.XCampo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    //4

}
