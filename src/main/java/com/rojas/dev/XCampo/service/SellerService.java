package com.rojas.dev.XCampo.service;

import com.rojas.dev.XCampo.entity.Seller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SellerService {
    ResponseEntity<?> insertSeller(Seller seller, List<MultipartFile> images);

    ResponseEntity<?> delete(Long id_seller);

    ResponseEntity<?> update(Seller seller);

    ResponseEntity<?> getSellerById(Long seller);

    ResponseEntity<?> getIdSellerByUser(Long user_id);
}
