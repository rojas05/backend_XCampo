package com.rojas.dev.XCampo.service.ServiceImp;

import com.rojas.dev.XCampo.entity.CartItem;
import com.rojas.dev.XCampo.entity.Product;
import com.rojas.dev.XCampo.entity.Shopping_cart;
import com.rojas.dev.XCampo.exception.EntityNotFoundException;
import com.rojas.dev.XCampo.repository.CartItemRepository;
import com.rojas.dev.XCampo.repository.ProductRepository;
import com.rojas.dev.XCampo.repository.ShoppingCartRepository;
import com.rojas.dev.XCampo.service.Interface.CartItemService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CartItemServiceImp implements CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public CartItem addProductToCart(CartItem cartItem) {
        Long clientId = cartItem.getCart().getClient().getId_client();
        Product product = existProductById(cartItem.getProduct().getId_product());
        int quantity = cartItem.getQuantity();

        var shoppingCart = existCartByUserId(clientId, cartItem);

        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setUnitPrice(quantity * product.getPrice());

        shoppingCart.addItem(cartItem);
        shoppingCartRepository.save(shoppingCart);

        return cartItem;
    }

    @Override
    public Shopping_cart createShoppingCart(CartItem cartItem) {
        if (cartItem == null || cartItem.getCart() == null || cartItem.getCart().getClient() == null) {
            throw new IllegalArgumentException("CartItem or its associated client cannot be null");
        }

        Shopping_cart shoppingCart = new Shopping_cart();
        shoppingCart.setClient(cartItem.getCart().getClient());
        shoppingCart.addItem(cartItem);
        shoppingCart.setDateAdded(LocalDate.now());

        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public Product existProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));
    }

    @Override
    @Transactional
    public Shopping_cart existCartByUserId(Long clientId, CartItem cartItem) {
        return shoppingCartRepository.findByClientId(clientId)
                .orElseGet(() -> createShoppingCart(cartItem));
    }
}
