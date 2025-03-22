package com.rojas.dev.XCampo.controller;

import com.rojas.dev.XCampo.entity.Seller;
import com.rojas.dev.XCampo.service.Interface.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/seller")
public class SellerController {

    @Autowired
    SellerService sellerService;

    /**
     * insertar un vendedor
     * @param seller
     * @param idRol
     * @return rol insertado
     */
    @PostMapping("{idRol}")
    public ResponseEntity<?> insertSeller(@RequestBody Seller seller, @PathVariable Long idRol){
        var sellerDate = sellerService.insertSeller(seller,idRol);
        return ResponseEntity.status(HttpStatus.CREATED).body(sellerDate);
    }

    /**
     * elimina el vendedor
     * @param id_seller
     * @return estado http
     */
    @DeleteMapping("{id_seller}")
    public ResponseEntity<?> deleteSeller(@PathVariable Long id_seller){
        return sellerService.delete(id_seller);
    }

    /**
     * atualizae vendedor
     * @param seller
     * @return estado http
     */
    @PatchMapping
    public ResponseEntity<?> updateSeller(@RequestBody Seller seller){
        return sellerService.update(seller);
    }

    /**
     * actualiza las imagenes de un vendedor
     * @param img
     * @param idSeller
     * @return
     */
    @PatchMapping("imgUpdate/{idSeller}")
    public ResponseEntity<?> updateSellerImg(@RequestParam("images") String img, @PathVariable Long idSeller){
        return sellerService.updateSellerImg(img,idSeller);
    }

    /**
     * obtiene el vendedor por el id de usuario
     * @param id_user
     * @return
     */
    @GetMapping("idUser/{id_user}")
    public ResponseEntity<?> getIdSellerByIdUser(@PathVariable Long id_user){
        return sellerService.getIdSellerByUser(id_user);
    }

    /**
     * obtiene el vendedor por su id
     * @param id_seller
     * @return vendedor
     */
    @GetMapping("{id_seller}")
    public ResponseEntity<?> getSellerById(@PathVariable Long id_seller){
        return sellerService.getSellerById(id_seller);
    }

    /**
     * obtiene vendedores por el municipio
     * @param city
     * @return lista de vendedores
     */
    @GetMapping("city/{city}")
    public ResponseEntity<?> getSellerByCity(@PathVariable String city){
        return sellerService.getSellerByCity(city);
    }

    /**
     * obtiene los vendedores por la vereda
     * @param location
     * @return lista de vendedores
     */
    @GetMapping("location/{location}")
    public ResponseEntity<?> getSellerByLocation(@PathVariable String location){
        return sellerService.getSellerByLocation(location);
    }
}

