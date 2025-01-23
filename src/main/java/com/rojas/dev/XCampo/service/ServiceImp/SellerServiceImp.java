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


    @Override
    public ResponseEntity<?> insertSeller(Seller seller, Long idRol) {
        Optional<Roles> result = rolesRepository.findById(idRol);
        try {

            if (result.isEmpty()) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while get the rol: ");

            seller.setRol(result.get());
            sellerRepository.save(seller);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id_seller}")
                    .buildAndExpand(seller.getId_seller())
                    .toUri();

            return ResponseEntity.created(location).body(seller);

        } catch (DataIntegrityViolationException e) {
            // Error si hay violaciones de integridad en los datos (ej. campos únicos)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Data integrity violation: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> delete(Long id_seller) {
        //  Si el vendedor no existe enviar repuesta
        if (!sellerRepository.existsById(id_seller)) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seller with id " + id_seller + " not found.");
        // existe, eliminar el vendedor
        sellerRepository.deleteById(id_seller);
        // Retornar código 200 (OK) indicando que se eliminó correctamente
        return ResponseEntity.status(HttpStatus.OK).body("Seller deleted successfully.");
    }

    @Override
    public ResponseEntity<?> update(Seller seller) {
        if (!sellerRepository.existsById(seller.getId_seller())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seller with id " + seller.getId_seller() + " not found.");
        }
        sellerRepository.updateSeller(
                seller.getId_seller(),
                String.valueOf(seller.getCoordinates()),
                seller.getLocation(),
                seller.getLocation_description(),
                seller.getName_store()
        );

        return ResponseEntity.status(HttpStatus.OK).body("Seller update successfully.");
    }

    @Override
    public ResponseEntity<?> getSellerById(Long seller_id) {
        Optional<Seller> seller = sellerRepository.findById(seller_id);
        if(seller.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seller with id " + seller_id + " not found.");

        return ResponseEntity.ok().body(seller);
    }

    @Override
    public ResponseEntity<?> getIdSellerByUser(Long user_id) {
        Optional<User> user = userRepository.findById(user_id);
        if (user.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id " + user_id + " not found.");

        Optional<Long> result = sellerRepository.getIdSellerByIdUser(user.get());
        if (result.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id " + user_id + " not found.");

        return ResponseEntity.ok().body(result);
    }

    @Override
    public ResponseEntity<?> updateSellerImg(String img, Long idSeller) {
        Optional<Seller> sellerVerify = sellerRepository.findById(idSeller);
        if (sellerVerify.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seller with id " + idSeller + " not found.");

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
    }

    @Override
    public BigDecimal getTotalEarnings(Long idSeller) {
        return null;
    }

    @Override
    public ResponseEntity<?> getSellerByCity(String city) {
        try {
            Optional<List<Seller>> result = sellerRepository.getSellerByCity(city);
            if(result.isPresent())
                return ResponseEntity.ok((result));
            else
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body("Na data");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while update the seller: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getSellerByLocation(String location) {
        try {
            Optional<List<Seller>> result = sellerRepository.getSellerByLocation(location);
            if(result.isPresent())
                return ResponseEntity.ok((result));
            else
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body("No data");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while update the seller: " + e.getMessage());
        }
    }
}
