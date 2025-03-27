package com.rojas.dev.XCampo.service.ServiceImp;

import com.rojas.dev.XCampo.dto.GetProductDTO;
import com.rojas.dev.XCampo.entity.Category;
import com.rojas.dev.XCampo.entity.Product;
import com.rojas.dev.XCampo.exception.EntityNotFoundException;
import com.rojas.dev.XCampo.exception.InvalidDataException;
import com.rojas.dev.XCampo.repository.ProductRepository;
import com.rojas.dev.XCampo.repository.SellerRepository;
import com.rojas.dev.XCampo.service.Interface.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
public class ProductServiceImp implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private CategoryServiceImp categoryRepository;

    @Override
    public Product createProduct(Product product, Long IdSeller, Long idCategory) {
        try{
            if (!sellerRepository.existsById(IdSeller))
                throw new EntityNotFoundException("Seller not found with ID: " + IdSeller);
            categoryRepository.exitsCategoryId(idCategory);

            return productRepository.save(product);
        } catch (Exception e){
            throw new EntityNotFoundException("Seller not found with ID: " + e);
        }
    }

    @Override
    public List<GetProductDTO> listProductIsSeller(Long IdSeller) {
        if(!sellerRepository.existsById(IdSeller)) throw new EntityNotFoundException("Seller not found with ID: " + IdSeller);
        return productRepository.findAllByIdSeller(IdSeller).stream()
                .map(this::convertProductsDTO)
                .toList();
    }

    @Override
    public GetProductDTO updateProductId(Long Id, Long idSeller, Product product) {
        productVerification(Id, idSeller);
        Product existingProduct = findId(Id);

        if (product.getName() != null) {
            existingProduct.setName(product.getName());
        }
        if (product.getDescription() != null) {
            existingProduct.setDescription(product.getDescription());
        }
        if (product.getUrlImage() != null) {
            existingProduct.setUrlImage(product.getUrlImage());
        }
        if (product.getPrice() != null) {
            existingProduct.setPrice(product.getPrice());
        }
        if (product.getStock() != null) {
            existingProduct.setStock(product.getStock());
        }
        if (product.getCategory() != null && product.getCategory().getId_category() != null) {
            Category category = categoryRepository.findIdCategory(product.getCategory().getId_category());
            existingProduct.setCategory(category);
        }
        Product updatedProduct = productRepository.save(existingProduct);
        return convertProductsDTO(updatedProduct);
    }

    @Override
    public ResponseEntity<?> updateProductImg(String img, Long Id, Long idSeller) {
        productVerification(Id, idSeller);

        productRepository.updateProductImg(Id, img);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id_product}")
                .buildAndExpand(Id)
                .toUri();
        return ResponseEntity.created(location).body("Product updated successfully");
    }

    @Override
    public Long updateProductStock(Long stock, Long Id, Long idSeller) {
        productVerification(Id, idSeller);
        Product existingProduct = findId(Id);

        var stockAvailable = existingProduct.getStock();
        if (stock > stockAvailable) throw new InvalidDataException("Quantity not available");

        Long newStock = stockAvailable - stock;
        existingProduct.setStock(newStock);

        productRepository.save(existingProduct);

        return newStock;
    }

    @Override
    public void deleteProductId(Long Id, Long idSeller) {
        productVerification(Id, idSeller);
        productRepository.deleteById(Id);
    }

    @Override
    public Product findId(Long Id) {
        return productRepository.findById(Id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + Id));
    }

    @Override
    public void productVerification(Long idProduct, Long idSeller) {
        if (!productRepository.existsByIdAndSellerId(idProduct, idSeller)) {
            throw new EntityNotFoundException(
                    "Product not found with ID: " + idProduct +
                            "or id Seller not register in Product: " + idSeller
            );
        }
    }

    @Override
    public ResponseEntity<?> search(String letter, String city) {
        try {
            List<Product> result = productRepository.search(letter,city);
            return ResponseEntity.ok().body(result);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    public GetProductDTO convertProductsDTO(Product product) {
        return new GetProductDTO(
                product.getId_product(),
                product.getName(),
                product.getDescription(),
                product.getStock(),
                product.getState(),
                product.getPrice(),
                product.getMeasurementUnit(),
                product.getUrlImage(),
                product.getSeller().getId_seller(),
                product.getCategory().getId_category(),
                product.getCategory().getName()
        );
    }

}
