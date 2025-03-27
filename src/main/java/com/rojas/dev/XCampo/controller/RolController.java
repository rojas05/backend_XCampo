package com.rojas.dev.XCampo.controller;

import com.rojas.dev.XCampo.entity.Roles;
import com.rojas.dev.XCampo.service.Interface.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * controlador para los roles
 */
@RestController
@RequestMapping("/rol")
public class RolController {

    @Autowired
    RolesService roleService;

    /**
     * insertar rol
     * @param rol
     * @param userId
     * @return
     */
    @PostMapping("{userId}")
    public ResponseEntity<?> insertRol(@RequestBody Roles rol, @PathVariable Long userId){
        return roleService.insert(rol, userId);
    }

    /**
     * obtiene los roles de un usuario
     * @param userId
     * @return list de roles
     */
    @GetMapping("{userId}")
    public ResponseEntity<?> getRolesByIdUser(@PathVariable Long userId){
        return roleService.getRolesByIdUser(userId);
    }

    /**
     * obtiene la lista de los id de los roles
     * @param userId
     * @return lista de id
     */
    @GetMapping("id/{userId}")
    public ResponseEntity<?> getIdRolesByIdUser(@PathVariable Long userId){
        return roleService.getRolesById(userId);
    }

    /**
     * inserta un ol de tipo seller
     * @param rol
     * @return rol insertado
     */
    @PostMapping("/seller")
    public ResponseEntity<?> insertNewRol(@RequestBody Roles rol){
        return roleService.insertRoles(rol);
    }

}