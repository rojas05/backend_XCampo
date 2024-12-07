package com.rojas.dev.XCampo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rojas.dev.XCampo.dto.GetShoppingCartDTO;
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
        Shopping_cart newProductsAdd = shoppingCartService.createShoppingCart(shoppingCart);
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
        Shopping_cart productQuantityShoppingCart = shoppingCartService.updateState(idShoppingCart, state);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Update state product");
        response.put("ShoppingCart", productQuantityShoppingCart);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{idClient}")
    public ResponseEntity<?> listAllProductsShoppingCart(@PathVariable Long idClient) {
        List<GetShoppingCartDTO> products = shoppingCartService.listAllProductsShoppingCart(idClient);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }


}
