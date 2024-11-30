package com.rojas.dev.XCampo.service.ServiceImp;

import com.rojas.dev.XCampo.entity.Roles;
import com.rojas.dev.XCampo.entity.Seller;
import com.rojas.dev.XCampo.entity.User;
import com.rojas.dev.XCampo.repository.RolesRepository;
import com.rojas.dev.XCampo.repository.SellerRepository;
import com.rojas.dev.XCampo.repository.UserRepository;
import com.rojas.dev.XCampo.service.Interface.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
public class SellerServiceImp implements SellerService {
    @Autowired
    RolesRepository rolesRepository;

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    UserRepository userRepository;

    // Verificar que que los datos no esten dupliados co el id

    @Override
    public ResponseEntity<?> insertSeller(Seller seller, Long idRol) {
        try {
            Optional<Roles> result = rolesRepository.findById(idRol);
            try {
                if (result.isPresent()){
                    seller.setRol(result.get());
                    sellerRepository.save(seller);
                    URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/{id_seller}")
                            .buildAndExpand(seller.getId_seller())
                            .toUri();
                    return ResponseEntity.created(location).body(seller);
                }else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Error occurred while get the rol: ");
                }
            } catch (DataIntegrityViolationException e) {
                // Error si hay violaciones de integridad en los datos (ej. campos únicos)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Data integrity violation: " + e.getMessage());
            } catch (Exception e) {
                // Cualquier otro error general
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error occurred while saving the seller: " + e.getMessage());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while get the rol: " + e.getMessage());
        }
    }



    @Override
    public ResponseEntity<?> delete(Long id_seller) {
        try {
            // Verificar si el vendedor existe
            if (sellerRepository.existsById(id_seller)) {
                // existe, eliminar el vendedor
                sellerRepository.deleteById(id_seller);
                // Retornar código 200 (OK) indicando que se eliminó correctamente
                return ResponseEntity.status(HttpStatus.OK).body("Seller deleted successfully.");
            } else {
                // Si no se encuentra el vendedor, retornar 404 (Not Found)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seller with id " + id_seller + " not found.");
            }
        } catch (Exception e) {
            // En caso de cualquier otro error, retornar código 500 (Internal Server Error)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while deleting the seller: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> update(Seller seller) {
        try {
            if (sellerRepository.existsById(seller.getId_seller())){
                sellerRepository.updateSeller(
                        seller.getId_seller(),
                        String.valueOf(seller.getCoordinates()),
                        seller.getLocation(),
                        seller.getLocation_description(),
                        seller.getName_store()
                );
                return ResponseEntity.status(HttpStatus.OK).body("Seller update successfully.");
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seller with id " + seller.getId_seller() + " not found.");
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while updating the seller: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getSellerById(Long seller_id) {
        try {
            Optional<Seller> seller = sellerRepository.findById(seller_id);
            if(seller.isPresent()){
                return ResponseEntity.ok().body(seller);
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seller with id " + seller_id + " not found.");
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while get the seller: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getIdSellerByUser(Long user_id) {
        try {
            Optional<User> user = userRepository.findById(user_id);
            if (user.isPresent()){
                Optional<Long> result = sellerRepository.getIdSellerByIdUser(user.get());
                if (result.isPresent()){
                    return ResponseEntity.ok().body(result);
                }else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id " + user_id + " not found.");
                }
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id " + user_id + " not found.");
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while get the id seller by id user: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> updateSellerImg(String img, Long idSeller) {
        Optional<Seller> sellerVerify = sellerRepository.findById(idSeller);
        if (sellerVerify.isPresent()){
            try {
                sellerRepository.updateSellerImg(idSeller,img);
                URI location = ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id_seller}")
                        .buildAndExpand(idSeller)
                        .toUri();
                return ResponseEntity.created(location).body("Seller update successfully.");
            }catch (Exception e){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error occurred while update the seller: " + e.getMessage());
            }
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seller with id " + idSeller + " not found.");
        }
    }

    @Override
    public BigDecimal getTotalEarnings(Long idSeller) {
        return null;
    }

}
