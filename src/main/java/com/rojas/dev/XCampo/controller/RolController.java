package com.rojas.dev.XCampo.controller;

import com.rojas.dev.XCampo.entity.Roles;
import com.rojas.dev.XCampo.service.Interface.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rol")
public class RolController {

    @Autowired
    RoleService roleService;

    @PostMapping("{user}")
    public ResponseEntity<?> insertRol(@RequestBody Roles rol, @PathVariable Long user){
        return roleService.insertNewRolUser(rol, user);
    }

    @GetMapping("{user}")
    public ResponseEntity<?> getRolesByIdUser(@PathVariable Long user){
        return roleService.getRolesByIdUser(user);
    }

    @PostMapping("seller")
    public ResponseEntity<?> insertNewRol(@RequestBody Roles rol){
        return roleService.insertRoles(rol);
    }

}
