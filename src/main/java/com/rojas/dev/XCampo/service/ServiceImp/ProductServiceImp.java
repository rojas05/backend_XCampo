package com.rojas.dev.XCampo.service.ServiceImp;

import com.rojas.dev.XCampo.entity.Product;
import com.rojas.dev.XCampo.exception.ProductNotFoundException;
import com.rojas.dev.XCampo.repository.ProductRepository;
import com.rojas.dev.XCampo.service.Interface.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImp implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserServiceImp userServiceImp;

    @Override
    public Product createProduct(Product product, Long IdSeller) {
        Long Id = product.getId_product();
        userServiceImp.existsUserId(IdSeller);

        if (productRepository.existsById(Id)) {
            throw new IllegalArgumentException("Product existed with ID: " + Id);
        }

        return productRepository.save(product);
    }

    @Override
    public List<Product> listProductIsSeller(Long IdSeller) {
        return productRepository.findAllByIdSeller(IdSeller);
    }

    @Override
    public Product updateProductId(Long Id, Long idSeller, Product product) {
        productVerification(Id, idSeller);

        product.setName(product.getName());
        product.setDescription(product.getDescription());
        product.setUrlImage(product.getUrlImage());

        return productRepository.save(product);
    }

    @Override
    public void deleteProductId(Long Id, Long idSeller) {
        productVerification(Id, idSeller);
        productRepository.deleteById(Id);
    }

    @Override
    public Product findId(Long Id) {
        return productRepository.findById(Id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + Id));
    }

    @Override
    public void productVerification(Long idProduct, Long idSeller) {
        if (!productRepository.existsByIdAndSellerId(idProduct, idSeller)) {
            throw new ProductNotFoundException(
                    "Product not found with ID: " + idProduct +
                    "or id Seller not register in Product: " + idSeller
            );
        }
    }

}
