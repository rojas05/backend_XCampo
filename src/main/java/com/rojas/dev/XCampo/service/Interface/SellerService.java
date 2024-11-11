package com.rojas.dev.XCampo.service.Interface;

import com.rojas.dev.XCampo.entity.Seller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SellerService {
    ResponseEntity<?> insertSeller(Seller seller, Long idRol);

    ResponseEntity<?> delete(Long id_seller);

    ResponseEntity<?> update(Seller seller);

    ResponseEntity<?> getSellerById(Long seller);

    ResponseEntity<?> getIdSellerByUser(Long user_id);

    ResponseEntity<?> updateSellerImg(String img,Long idSeller);
}
