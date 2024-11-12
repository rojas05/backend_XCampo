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

    @Override
    public Product createProduct(Product product) {
        Long Id = product.getId_product();

        if (productRepository.existsById(Id)) {
            throw new IllegalArgumentException("Product existed with ID: " + Id);
        }

        return productRepository.save(product);
    }

    @Override
    public List<Product> listProduct() {
        return productRepository.findAll();
    }

    @Override
    public Product updateProductId(Long Id, Product product) {
        existsProduct(Id);

        product.setName(product.getName());
        product.setDescription(product.getDescription());
        product.setUrlImage(product.getUrlImage());

        return productRepository.save(product);
    }

    @Override
    public void deleteProductId(Long Id) {
        existsProduct(Id);
        productRepository.deleteById(Id);
    }

    @Override
    public Product findId(Long Id) {
        return productRepository.findById(Id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + Id));
    }

    @Override
    public void existsProduct(Long Id) {
        if (!productRepository.existsById(Id)) {
            throw new ProductNotFoundException("Product not found with ID: " + Id);
        }
    }

}
