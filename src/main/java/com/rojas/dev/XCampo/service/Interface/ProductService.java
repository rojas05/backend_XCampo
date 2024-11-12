package com.rojas.dev.XCampo.service.Interface;

import com.rojas.dev.XCampo.entity.Product;

import java.util.List;

public interface ProductService {

    Product createProduct(Product product);
    List<Product> listProduct();
    Product updateProductId(Long Id, Product product);
    void deleteProductId(Long Id);
    Product findId(Long Id);
    void existsProduct(Long Id);

}
