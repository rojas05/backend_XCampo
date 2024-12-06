package com.rojas.dev.XCampo.service.Interface;

import com.rojas.dev.XCampo.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User addUser(User newUser);

    void deleteUserID(Long Id);

    User updateUser(Long Id, User postUser);

    Optional<User> findByIdUser(Long Id);

    void existsUserId(Long Id);

    List<User> listAllUser();

    ResponseEntity<?> getNFSidByIdUser(Long id_user);

}
