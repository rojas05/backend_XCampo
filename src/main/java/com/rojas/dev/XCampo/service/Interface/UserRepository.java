package com.rojas.dev.XCampo.service.Interface;

import com.rojas.dev.XCampo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {


}
