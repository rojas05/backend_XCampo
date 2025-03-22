package com.rojas.dev.XCampo.controller;

import com.rojas.dev.XCampo.entity.User;
import com.rojas.dev.XCampo.enumClass.UserRole;
import com.rojas.dev.XCampo.service.Interface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * controlador de usuario
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * ingresa un nuevo usuario
     * @param newUser
     * @return
     */
    @PostMapping()
    public ResponseEntity<?> registerUser(@RequestBody User newUser) {
        User createUser = userService.addUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createUser);
    }

    /**
     * obtiene la info de un usuario
     * @param id
     * @return usuario
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> findByUserId(@PathVariable Long id) {
        Optional<User> userId = userService.findByIdUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(userId.get());
    }

    /**
     * elimina un usario
     * @param userId
     * @return
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        userService.deleteUserID(userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * actualiza un suario
     * @param userId
     * @param updatedUser
     * @return estado http
     */
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody User updatedUser) {
        User user = userService.updateUser(userId, updatedUser);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "CartItem updated successfully");
        response.put("cartItem", user);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * lista los usuarios
     * @return lista de usuarios
     */
    @GetMapping()
    public ResponseEntity<?> listAll() {
        List<User> user = userService.listAllUser();
        return ResponseEntity.status(HttpStatus.FOUND).body(user);
    }

    /**
     * obtener el token de notificaciones
     * @param id
     * @return token de notificaciones
     */
    @GetMapping("nfs/{id}")
    public ResponseEntity<?> getNFSidByIdUser(@PathVariable Long id){
        return userService.getNFSidByIdUser(id);
    }

    /**
     * obtener el token de notificaciones
     * @param role
     * @return notificaciones
     */
    @GetMapping("nfs/rol")
    public ResponseEntity<?> getNFSidByUserRol(@RequestParam UserRole role){
        try {
            List<String> result = userService.findFcmTokensByRole(role);
            return ResponseEntity.ok(result);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    /**
     * actualiza el token de notificaciones de un usuario
     * @param user
     * @return estado http
     */
    @PatchMapping("nfs")
    public ResponseEntity<?> updateNfs(@RequestBody User user){
        return userService.updateNfs(user);
    }
}
