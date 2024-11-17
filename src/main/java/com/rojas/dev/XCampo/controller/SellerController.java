package com.rojas.dev.XCampo.controller;

import com.rojas.dev.XCampo.entity.Seller;
import com.rojas.dev.XCampo.service.Interface.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/seller")
public class SellerController {

    @Autowired
    SellerService sellerService;

    @PostMapping("{idRol}")
    public ResponseEntity<?> insertSeller(@RequestBody Seller seller, @PathVariable Long idRol){
        return sellerService.insertSeller(seller,idRol);
    }

    @DeleteMapping("{id_seller}")
    public ResponseEntity<?> deleteSeller(@PathVariable Long id_seller){
        return sellerService.delete(id_seller);
    }

    @PatchMapping
    public ResponseEntity<?> updateSeller(@RequestBody Seller seller){
        return sellerService.update(seller);
    }

    @PatchMapping("imgUpdate/{idSeller}")
    public ResponseEntity<?> updateSellerImg(@RequestParam("img") String img, @PathVariable Long idSeller){
        return sellerService.updateSellerImg(img,idSeller);
    }

    @GetMapping("idUser/{id_user}")
    public ResponseEntity<?> getIdSellerByIdUser(@PathVariable Long id_user){
        return sellerService.getIdSellerByUser(id_user);
    }

    @GetMapping("{id_seller}")
    public ResponseEntity<?> getSellerById(@PathVariable Long id_seller){
        return sellerService.getSellerById(id_seller);
    }

}

