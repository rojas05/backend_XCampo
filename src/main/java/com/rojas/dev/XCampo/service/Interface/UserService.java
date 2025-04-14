package com.rojas.dev.XCampo.service.Interface;

import com.rojas.dev.XCampo.entity.User;
import com.rojas.dev.XCampo.enumClass.UserRole;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User addUser(User newUser);

    void deleteUserID(Long Id);

    ResponseEntity<?> updateUser(User postUser);

    Optional<User> findByIdUser(Long Id);

    void existsUserId(Long Id);

    List<User> listAllUser();

    ResponseEntity<?> getNFSidByIdUser(Long id_user);

    ResponseEntity<?> updateNfs(User user);

    List<String> findFcmTokensByRole(UserRole role);

    String findFcmTokensByIdClient(Long id);

    String findFcmTokensByIdSeller(Long id);
}
