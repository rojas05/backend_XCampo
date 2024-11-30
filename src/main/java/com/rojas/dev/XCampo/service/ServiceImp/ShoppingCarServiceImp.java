package com.rojas.dev.XCampo.service.ServiceImp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rojas.dev.XCampo.dto.ShoppingCartDTO;
import com.rojas.dev.XCampo.entity.CartItem;
import com.rojas.dev.XCampo.entity.Shopping_cart;
import com.rojas.dev.XCampo.exception.EntityNotFoundException;
import com.rojas.dev.XCampo.repository.CartItemRepository;
import com.rojas.dev.XCampo.repository.ClientRepository;
import com.rojas.dev.XCampo.repository.ProductRepository;
import com.rojas.dev.XCampo.repository.ShoppingCartRepository;
import com.rojas.dev.XCampo.service.Interface.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCarServiceImp implements ShoppingCartService {
    

    @Autowired
    private ShoppingCartRepository shoppingCarRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public Shopping_cart addProduct(ShoppingCartDTO shoppingCart) throws JsonProcessingException {
        var client = clientRepository.findById(shoppingCart.getClientId())
                .orElseThrow(() -> new EntityNotFoundException("Client ID NOT found: " + shoppingCart.getClientId()));
        CartItem carItem = cartItemRepository.findById(shoppingCart.getItemId())
                .orElseThrow(() -> new EntityNotFoundException("cart item not found"));
        Shopping_cart addShoppingCart = new Shopping_cart();
        addShoppingCart.setClient(client);

        String result = new ObjectMapper().writeValueAsString(carItem);
        System.out.println("------------------------------------------------------" + result);
        addShoppingCart.getItems().add(carItem);

        addShoppingCart.setDateAdded(shoppingCart.getDateAdded());
        addShoppingCart.setStatus(shoppingCart.isStatus());

        return shoppingCarRepository.save(addShoppingCart);
    }

    @Override
    public void deleteProduct(Long idShoppingCar) {
        Shopping_cart entity = findByIdShoppingCard(idShoppingCar);
        existsClient(entity.getClient().getId_client());

        shoppingCarRepository.deleteById(idShoppingCar);
    }

    @Override
    public Shopping_cart updateState(Long idShoppingCar, boolean state) {
        Shopping_cart entity = findByIdShoppingCard(idShoppingCar);;
        entity.setStatus(state);
        return shoppingCarRepository.save(entity);
    }

    @Override
    public Shopping_cart findByIdShoppingCard(Long id) {
        return shoppingCarRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Carrito no encontrado con ID: " + id));
    }

    @Override
    public List<Shopping_cart> listAllProductsShoppingCart(Long idClient) {
        return shoppingCarRepository.findByClientId(idClient);
    }

    public void exitsShoppingCar(Long id) {
        if(!shoppingCarRepository.existsById(id)) {
            throw new EntityNotFoundException("Carrito no encontrado con ID: " + id);
        }

    }

    public void existsClient(Long idClient) {
        if (!clientRepository.existsById(idClient)) {
            throw new IllegalStateException("El cliente con el ID no existe: " + idClient);
        }
    }

}
