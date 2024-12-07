package com.rojas.dev.XCampo.service.ServiceImp;

import com.rojas.dev.XCampo.dto.CartItemDTO;
import com.rojas.dev.XCampo.entity.CartItem;
import com.rojas.dev.XCampo.entity.Product;
import com.rojas.dev.XCampo.entity.Shopping_cart;
import com.rojas.dev.XCampo.exception.EntityNotFoundException;
import com.rojas.dev.XCampo.repository.CartItemRepository;
import com.rojas.dev.XCampo.repository.ProductRepository;
import com.rojas.dev.XCampo.repository.ShoppingCartRepository;
import com.rojas.dev.XCampo.service.Interface.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        /*
        Long clientId = cartItem.getCart().getClient().getId_client();
        Product product = existProductById(cartItem.getProduct().getId_product());
        int quantity = cartItem.getQuantity();

        var shoppingCart = existCartByUserId(clientId, cartItem);

        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setUnitPrice(quantity * product.getPrice());

        shoppingCart.addItem(cartItem);
        */
        cartItemRepository.save(cartItem);

        return cartItem;
    }

    @Override
    public Shopping_cart createShoppingCart(CartItem cartItem) {
        if (cartItem == null || cartItem.getCart() == null || cartItem.getCart().getClient() == null) {
            throw new IllegalArgumentException("CartItem or its associated client cannot be null");
        }
    public CartItem addProductToCart(CartItemDTO cartItem) {
        Shopping_cart cart = findShoppingID(cartItem.getCardId());
        Product product = findProductID(cartItem.getProductId());

        CartItem addCartsItem = new CartItem();
        var quantity = cartItem.getQuantity();
        addCartsItem.setProduct(product);
        addCartsItem.setCart(cart);
        addCartsItem.setQuantity(quantity);
        addCartsItem.setUnitPrice(quantity * product.getPrice());

        System.out.println("---------------" + cartItem.getCardId());
        System.out.println("----------------" + cart);

        return cartItemRepository.save(addCartsItem);
    }


    public Product findProductID(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    public Shopping_cart findShoppingID(Long cartId) {
        return shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping not found: " + cartId));
    }
}
