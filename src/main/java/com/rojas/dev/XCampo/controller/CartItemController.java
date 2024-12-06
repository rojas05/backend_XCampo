package com.rojas.dev.XCampo.controller;

import com.rojas.dev.XCampo.entity.CartItem;
import com.rojas.dev.XCampo.repository.CartItemRepository;
import com.rojas.dev.XCampo.service.Interface.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cartItem")
public class CartItemController {

    @Autowired
    private CartItemRepository cartItemRepository;

    @PostMapping
    public ResponseEntity<?>insert(@RequestBody CartItem cartItem){
        try {
              cartItemRepository.save(cartItem);
            return ResponseEntity.ok().body(cartItem);
        }catch (Exception e){
            System.err.println(e);
            return ResponseEntity.ok().body(e);
        }
    }

}
