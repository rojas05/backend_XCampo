package com.rojas.dev.XCampo.controller;

import com.rojas.dev.XCampo.entity.User;
import com.rojas.dev.XCampo.service.Service.UserService;
import com.rojas.dev.XCampo.service.ServiceImp.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User newUser) {
        User createUser = userService.addUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findIdUser(@PathVariable Long id) {
        try {
            Optional<User> userFound = userService.findByIdUser(id);
            return ResponseEntity.status(HttpStatus.FOUND).body("User found: " + userFound);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found user:" + id);
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUserID(userId);
            return ResponseEntity.status(HttpStatus.OK).body("user deleted with id: " + userId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id User not found: " + e.getMessage());
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody User updatedUser) {
        try {
            User user = userService.updateUser(userId, updatedUser);
            return ResponseEntity.status(HttpStatus.CREATED).body("Update user: " + user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found user:" + userId);
        }
    }


}
