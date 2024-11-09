package com.rojas.dev.XCampo.controller;

import com.rojas.dev.XCampo.entity.Product;
import com.rojas.dev.XCampo.service.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<?> newProduct(@RequestBody Product product) {
        Product newProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Created new product: " + newProduct);
    }

    @PutMapping("/{Id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long Id, @RequestBody Product product) {
        Product postProduct = productService.updateProductId(Id, product);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Product Update: " + postProduct);
    }

    @DeleteMapping("/{Id}")
    public ResponseEntity<?> deleteProductId(@PathVariable Long Id) {
        productService.deleteProductId(Id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body("Product delete with ID: " + Id);
    }

    @GetMapping("/{Id}")
    public ResponseEntity<?> findIdProduct(@PathVariable Long Id) {
        Product findProduct = productService.findId(Id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(findProduct);
    }

    @GetMapping("/listAll")
    public ResponseEntity<?> listProduct() {
        List<Product> products = productService.listProduct();
        return ResponseEntity.status(HttpStatus.OK)
                .body(products);
    }

}
