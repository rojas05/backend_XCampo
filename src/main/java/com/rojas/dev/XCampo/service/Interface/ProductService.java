package com.rojas.dev.XCampo.service.Interface;

import com.rojas.dev.XCampo.dto.GetProductDTO;
import com.rojas.dev.XCampo.entity.Product;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {

    Product createProduct(Product product, Long IdSeller, Long idCategory);

    List<GetProductDTO> listProductIsSeller(Long IdSeller);

    GetProductDTO updateProductId(Long Id, Long idSeller, Product product);

    ResponseEntity<?> updateProductImg(String img, Long Id, Long idSeller);

    Long updateProductStock(Long stock, Long Id, Long idSeller);

    void deleteProductId(Long Id, Long idSeller);

    Product findId(Long Id);

    void productVerification(Long Id, Long idSeller);

    ResponseEntity<?> search(String letter, String city);

}
