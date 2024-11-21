package com.rojas.dev.XCampo.controller;

import com.rojas.dev.XCampo.entity.Shopping_cart;
import com.rojas.dev.XCampo.service.Interface.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ShoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping
    public ResponseEntity<?> addProductShoppingCart(@RequestBody Shopping_cart shoppingCart) {
        Shopping_cart newProductsAdd = shoppingCartService.addProduct(shoppingCart);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Create shopping cart: " +  newProductsAdd);
    }

    @DeleteMapping("/{idProduct}")
    public ResponseEntity<?> deleteProductShoppingCart(@PathVariable Long idProduct) {
        shoppingCartService.deleteProduct(idProduct);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body("removed product shopping cart");
    }

    @PutMapping("/{idShoppingCart}/{amount}")
    public ResponseEntity<?> updateQuantity(@PathVariable Long idShoppingCart, @PathVariable Long amount) {
        Shopping_cart productQuantityShoppingCart = shoppingCartService.updateQuantity(idShoppingCart, amount);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Update amount product: " + productQuantityShoppingCart);
    }

    @GetMapping("/{idClient}")
    public ResponseEntity<?> listAllProductsShoppingCart(@PathVariable Long idClient) {
        List<Shopping_cart> products = shoppingCartService.listAllProductsShoppingCart(idClient);
        return ResponseEntity.status(HttpStatus.OK)
                .body(products);
    }


}
