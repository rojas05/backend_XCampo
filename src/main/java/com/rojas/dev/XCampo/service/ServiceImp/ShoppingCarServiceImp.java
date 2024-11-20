package com.rojas.dev.XCampo.service.ServiceImp;

import com.rojas.dev.XCampo.entity.Shopping_cart;
import com.rojas.dev.XCampo.exception.ShoppingCartNotFoundException;
import com.rojas.dev.XCampo.repository.ClientRepository;
import com.rojas.dev.XCampo.repository.ProductRepository;
import com.rojas.dev.XCampo.repository.ShoppingCarRepository;
import com.rojas.dev.XCampo.service.Interface.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCarServiceImp implements ShoppingCartService {

    @Autowired
    private ShoppingCarRepository shoppingCarRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public Shopping_cart addProduct(Shopping_cart shoppingCart) {
        /*
        var idProduct = shoppingCart.getProducts().getId_product();
        var idClient = shoppingCart.getClients().getId_client();

        fkVerification(idClient, idProduct);

        return shoppingCarRepository.save(shoppingCart);
        * */
        return null;
    }

    public Long updateStockProduct(Long amount){


        return 0L;
    }


    @Override
    public void deleteProduct(Long idShoppingCar) {
        /*
        * Shopping_cart entity = findByIdShoppingCard(idShoppingCar);
        fkVerification(entity.getClients().getId_client(), entity.getProducts().getId_product());
        * */

        shoppingCarRepository.deleteById(idShoppingCar);
    }

    @Override
    public Shopping_cart updateQuantity(Long idShoppingCar, Long amount) {
        Shopping_cart entity = findByIdShoppingCard(idShoppingCar);
        //entity.setAmount(amount);
        return shoppingCarRepository.save(entity);
    }

    @Override
    public Shopping_cart findByIdShoppingCard(Long id) {
        return shoppingCarRepository.findById(id).
                orElseThrow(() -> new ShoppingCartNotFoundException("Carrito no encontrado con ID: " + id));
    }

    @Override
    public void exitsShoppingCar(Long id) {
        boolean existRegister = shoppingCarRepository.existsById(id);

        if(!existRegister) {
            throw new ShoppingCartNotFoundException("Carrito no encontrado con ID: " + id);
        }

    }

    public void fkVerification(Long idClient, Long idProduct) {
        boolean exists = shoppingCarRepository.existsByClientAndProduct(idClient, idProduct);
        if (exists) {
            throw new IllegalStateException("El producto ya está en el carrito del cliente.");
        }
    }


}
