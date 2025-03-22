package com.rojas.dev.XCampo.controller;

import com.rojas.dev.XCampo.dto.CartItemDTO;
import com.rojas.dev.XCampo.service.Interface.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * controlador para item de carrito
 */
@RestController
@RequestMapping("/cartItem")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    /**
     * endPoint para registro de un nuevo item
     * @param cartItem
     * @return item ingresado
     */
    @PostMapping()
    public ResponseEntity<?> InsertItemCart(@RequestBody CartItemDTO cartItem) {
        cartItemService.addProductToCart(cartItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItem);
    }

    /**
     * endPoint para listar los items de cun carrito
     * @param idClient
     * @return lista de items
     */
    @GetMapping("all/{idClient}")
    public ResponseEntity<?> findByIdCartItemAll(@PathVariable Long idClient) {
        var cartItem = cartItemService.listAllCartItem(idClient);
        return ResponseEntity.status(HttpStatus.OK).body(cartItem);
    }

    /**
     * actualiza la cantidad de productos en un item
     * @param idItemCart
     * @param quantity
     * @return
     */
    @PutMapping("{idItemCart}/{quantity}")
    public ResponseEntity<?> updateItemCart(@PathVariable Long idItemCart, @PathVariable Long quantity) {
        var cartItem = cartItemService.updateCarItem(idItemCart, quantity);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "CartItem updated successfully");
        response.put("cartItem", cartItem);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * elimina un item del carrito
     * @param idItemCart
     * @return
     */
    @DeleteMapping("{idItemCart}")
    public ResponseEntity<?> deleteIdCartItem(@PathVariable Long idItemCart) {
        cartItemService.deleteCartItem(idItemCart);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Delete cart item with id: " + idItemCart);
    }

    /**
     * listar items de un carrito en espesifico
     * @param idShoppingCart
     * @return
     */
    @GetMapping("{idShoppingCart}")
    public ResponseEntity<?> getItemsCart(@PathVariable Long idShoppingCart) {
        Long response = cartItemService.getItemsTotal(idShoppingCart);
        return ResponseEntity.ok(response);
    }

}
