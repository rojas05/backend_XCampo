package com.rojas.dev.XCampo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rojas.dev.XCampo.dto.GetShoppingCartDTO;
import com.rojas.dev.XCampo.dto.ResponseCartItemDTO;
import com.rojas.dev.XCampo.dto.ShoppingCartDTO;
import com.rojas.dev.XCampo.entity.Shopping_cart;
import com.rojas.dev.XCampo.repository.CartItemRepository;
import com.rojas.dev.XCampo.repository.ShoppingCartRepository;
import com.rojas.dev.XCampo.service.Interface.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ShoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @PostMapping("/add")
    public ResponseEntity<?> addProductShoppingCart(@RequestBody ShoppingCartDTO shoppingCart) throws JsonProcessingException {
        ResponseCartItemDTO newProductsAdd = shoppingCartService.createShoppingCart(shoppingCart);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newProductsAdd);
    }

    @DeleteMapping("/{idProduct}")
    public ResponseEntity<?> deleteProductShoppingCart(@PathVariable Long idProduct) {
        shoppingCartService.deleteProduct(idProduct);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body("removed product shopping cart");
    }

    @PutMapping("/{idShoppingCart}/{state}")
    public ResponseEntity<?> updateState(@PathVariable Long idShoppingCart, @PathVariable boolean state) {
        shoppingCartService.updateState(idShoppingCart, state);

        return ResponseEntity.status(HttpStatus.OK)
                .body("Update state product");
    }

    @PatchMapping("/cart/{cartId}/items/{itemId}")
    public ResponseEntity<Shopping_cart> addItemToCart(@PathVariable Long cartId, @PathVariable Long itemId) {
        Shopping_cart cart = shoppingCartService.addItemToCart(cartId, itemId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(cart);
    }

    @GetMapping("/{idClient}")
    public ResponseEntity<?> listAllProductsShoppingCart(@PathVariable Long idClient) {
        Shopping_cart products = shoppingCartService.findByIdShoppingCard(idClient);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @GetMapping("get/{idClient}")
    public ResponseEntity<?> getIdCart(@PathVariable Long idClient) {
        return shoppingCartService.getIdCartByIdUser(idClient);
    }
}
