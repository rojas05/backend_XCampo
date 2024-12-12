package com.rojas.dev.XCampo.service.Interface;

import com.rojas.dev.XCampo.dto.GetProductDTO;
import com.rojas.dev.XCampo.entity.Product;

import java.util.List;

public interface ProductService {

    Product createProduct(Product product, Long IdSeller, Long idCategory);

    List<GetProductDTO> listProductIsSeller(Long IdSeller);

    Product updateProductId(Long Id, Long idSeller, Product product);

    void deleteProductId(Long Id, Long idSeller);

    Product findId(Long Id);

    void productVerification(Long Id, Long idSeller);

}
