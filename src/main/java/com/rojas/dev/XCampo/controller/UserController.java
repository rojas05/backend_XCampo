package com.rojas.dev.XCampo.controller;

import com.rojas.dev.XCampo.entity.User;
import com.rojas.dev.XCampo.service.Interface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping()
    public ResponseEntity<?> registerUser(@RequestBody User newUser) {
        User createUser = userService.addUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findByUserId(@PathVariable Long id) {
        Optional<User> userId = userService.findByIdUser(id);
        return ResponseEntity.status(HttpStatus.FOUND).body(userId);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        userService.deleteUserID(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody User updatedUser) {
        User user = userService.updateUser(userId, updatedUser);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "CartItem updated successfully");
        response.put("cartItem", user);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping()
    public ResponseEntity<?> listAll() {
        List<User> user = userService.listAllUser();
        return ResponseEntity.status(HttpStatus.FOUND).body(user);
    }

    @GetMapping("nfs/{id}")
    public ResponseEntity<?> getNFSidByIdUser(@PathVariable Long id){
        return userService.getNFSidByIdUser(id);
    }


}
