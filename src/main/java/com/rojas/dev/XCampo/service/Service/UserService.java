package com.rojas.dev.XCampo.service.Service;

import com.rojas.dev.XCampo.entity.User;

import java.util.Optional;

public interface UserService {

    User addUser(User newUser);
    void deleteUserID(Long Id);
    User updateUser(Long Id, User postUser);
    Optional<User> findByIdUser(Long Id);
    void existsUserId(Long Id);

}
