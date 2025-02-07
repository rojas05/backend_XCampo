package com.rojas.dev.XCampo.service.ServiceImp;

import com.rojas.dev.XCampo.dto.GetProductDTO;
import com.rojas.dev.XCampo.entity.Product;
import com.rojas.dev.XCampo.exception.EntityNotFoundException;
import com.rojas.dev.XCampo.exception.InvalidDataException;
import com.rojas.dev.XCampo.repository.CategoryRepository;
import com.rojas.dev.XCampo.repository.ProductRepository;
import com.rojas.dev.XCampo.repository.SellerRepository;
import com.rojas.dev.XCampo.service.Interface.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImp implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Product createProduct(Product product, Long IdSeller, Long idCategory) {
        if(!sellerRepository.existsById(IdSeller)) throw new EntityNotFoundException("Seller not found with ID: " + IdSeller);
        if(!categoryRepository.existsById(idCategory)) throw new InvalidDataException("Category not found with ID: " + idCategory);

        return productRepository.save(product);
    }

    @Override
    public List<GetProductDTO> listProductIsSeller(Long IdSeller) {
        if(!sellerRepository.existsById(IdSeller)) throw new EntityNotFoundException("Seller not found with ID: " + IdSeller);
        return productRepository.findAllByIdSeller(IdSeller).stream()
                .map(this::convertProductsDTO)
                .toList();
    }

    @Override
    public Product updateProductId(Long Id, Long idSeller, Product product) {
        productVerification(Id, idSeller);

        product.setName(product.getName());
        product.setDescription(product.getDescription());
        product.setUrlImage(product.getUrlImage());

        return productRepository.save(product);
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
                product.getCategory().getId_category()
        );
    }

}
