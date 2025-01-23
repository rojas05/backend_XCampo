package com.rojas.dev.XCampo.controller;

import com.rojas.dev.XCampo.entity.Roles;
import com.rojas.dev.XCampo.service.Interface.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rol")
public class RolController {

    @Autowired
    RolesService roleService;

    @PostMapping("{userId}")
    public ResponseEntity<?> insertRol(@RequestBody Roles rol, @PathVariable Long userId){
        return roleService.insert(rol, userId);
    }

    @GetMapping("{userId}")
    public ResponseEntity<?> getRolesByIdUser(@PathVariable Long userId){
        return roleService.getRolesByIdUser(userId);
    }

    @PostMapping("/seller")
    public ResponseEntity<?> insertNewRol(@RequestBody Roles rol){
        return roleService.insertRoles(rol);
    }

}