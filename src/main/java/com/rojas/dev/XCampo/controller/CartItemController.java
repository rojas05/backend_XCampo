package com.rojas.dev.XCampo.controller;

import com.rojas.dev.XCampo.dto.CartItemDTO;
import com.rojas.dev.XCampo.entity.CartItem;
import com.rojas.dev.XCampo.repository.CartItemRepository;
import com.rojas.dev.XCampo.service.Interface.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cartItem")
public class CartItemController {

    @Autowired
    private CartItemRepository cartItemService;

    @PostMapping()
    public ResponseEntity<?> addProductToCart(@RequestBody CartItem cartItem) {
        // CartItem addedItem = cartItemService.addProductToCart(cartItem);
        // return ResponseEntity.status(HttpStatus.CREATED).body(addedItem);
        try {
            cartItemService.save(cartItem);
            return ResponseEntity.status(HttpStatus.OK).body(cartItem);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }

    }
}
