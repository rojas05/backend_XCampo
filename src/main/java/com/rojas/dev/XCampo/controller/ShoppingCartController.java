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

import java.util.List;

@RestController
@RequestMapping("/ShoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    /**
     * agregar productos al carrito
     * @param shoppingCart
     * @return estado http
     * @throws JsonProcessingException
     */
    @PostMapping("/add")
    public ResponseEntity<?> addProductShoppingCart(@RequestBody ShoppingCartDTO shoppingCart) throws JsonProcessingException {
        ResponseCartItemDTO newProductsAdd = shoppingCartService.createShoppingCart(shoppingCart);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newProductsAdd);
    }

    /**
     * elimina el carrito
     * @param idProduct
     * @return
     */
    @DeleteMapping("/{idProduct}")
    public ResponseEntity<?> deleteProductShoppingCart(@PathVariable Long idProduct) {
        shoppingCartService.deleteProduct(idProduct);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body("removed product shopping cart");
    }

    /**
     * actualizar el estado de un carrito
     * @param idShoppingCart
     * @param state
     * @return
     */
    @PutMapping("/{idShoppingCart}/{state}")
    public ResponseEntity<?> updateState(@PathVariable Long idShoppingCart, @PathVariable boolean state) {
        shoppingCartService.updateState(idShoppingCart, state);

        return ResponseEntity.status(HttpStatus.OK)
                .body("Update state product");
    }

    /**
     * actualiza un producto de un carrito
     * @param cartId
     * @param itemId
     * @return
     */
    @PatchMapping("/cart/{cartId}/items/{itemId}")
    public ResponseEntity<Shopping_cart> addItemToCart(@PathVariable Long cartId, @PathVariable Long itemId) {
        Shopping_cart cart = shoppingCartService.addItemToCart(cartId, itemId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(cart);
    }

    /**
     * lista de productos en carrito por id del cliente
     * @param idClient
     * @return lista de productos
     */
    @GetMapping("/{idClient}")
    public ResponseEntity<?> listAllProductsShoppingCart(@PathVariable Long idClient) {
        List<GetShoppingCartDTO> products = shoppingCartService.listAllProductsShoppingCart(idClient);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    /**
     * lista de carritos
     * @param idUser
     * @return lista de carritos
     */
    @GetMapping("/id/{idUser}")
    public ResponseEntity<?> getIdCartByIdUser(@PathVariable Long idUser) {
        return shoppingCartService.getIdCartByIdUser(idUser);
    }

}
