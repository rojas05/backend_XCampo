package com.rojas.dev.XCampo.service.Interface;

import com.rojas.dev.XCampo.entity.Product;

import java.util.List;

public interface ProductService {

    Product createProduct(Product product, Long IdSeller, Long idCategory);

    List<Product> listProductIsSeller(Long IdSeller);

    Product updateProductId(Long Id, Long idSeller, Product product);

    Product updateImageProduct(Long id, Long idSeller, String url);

    void deleteProductId(Long Id, Long idSeller);

    Product findId(Long Id);

    void productVerification(Long Id, Long idSeller);

}
