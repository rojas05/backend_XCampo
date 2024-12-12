package com.rojas.dev.XCampo.controller;

import com.rojas.dev.XCampo.dto.CartItemDTO;
import com.rojas.dev.XCampo.service.Interface.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/cartItem")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    @PostMapping()
    public ResponseEntity<?> addItemCart(@RequestBody CartItemDTO cartItem) {
        cartItemService.addProductToCart(cartItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItem);
    }

    @GetMapping("all/{idClient}")
    public ResponseEntity<?> findByIdCartItemAll(@PathVariable Long idClient) {
        var cartItem = cartItemService.listAllCartItem(idClient);
        return ResponseEntity.status(HttpStatus.FOUND).body(cartItem);
    }

    @PutMapping("{idItemCart}/{quantity}")
    public ResponseEntity<?> updateItemCart(@PathVariable Long idItemCart, @PathVariable int quantity) {
        var cartItem = cartItemService.updateCarItem(idItemCart, quantity);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "CartItem updated successfully");
        response.put("cartItem", cartItem);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("{idItemCart}")
    public ResponseEntity<?> deleteIdCartItem(@PathVariable Long idItemCart) {
        cartItemService.deleteCartItem(idItemCart);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Delete cart item with id: " + idItemCart);
    }

}
