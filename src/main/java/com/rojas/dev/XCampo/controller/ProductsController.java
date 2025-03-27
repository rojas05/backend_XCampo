package com.rojas.dev.XCampo.controller;

import com.rojas.dev.XCampo.dto.GetProductDTO;
import com.rojas.dev.XCampo.entity.Product;
import com.rojas.dev.XCampo.service.Interface.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductService productService;

    /**
     * crear nuevo producto
     * @param product
     * @return producto
     */
    @PostMapping
    public ResponseEntity<?> insertProduct(@RequestBody Product product) {
        var idSeller = product.getSeller().getId_seller();
        var idCategory = product.getCategory().getId_category();

        Product newProduct = productService.createProduct(product, idSeller, idCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    /**
     * actualizar producto
     * @param IdProduct
     * @param product
     * @return estado http
     */
    @PutMapping("/{IdProduct}")
    public ResponseEntity<?> updateProduct(@PathVariable Long IdProduct, @RequestBody Product product) {
        var idSeller = product.getSeller().getId_seller();
        GetProductDTO postProduct = productService.updateProductId(IdProduct, idSeller, product);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Product updated successfully");
        response.put("Product", postProduct);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    /**
     * actualizar la imagen del producto
     * @param img
     * @param IdProduct
     * @param idSeller
     * @return estado http
     */
    @PatchMapping("/{IdProduct}/imgUpdate/{idSeller}")
    public ResponseEntity<?> updateProductImg(@RequestParam("images") String img, @PathVariable Long IdProduct, @PathVariable Long idSeller) {
        return productService.updateProductImg(img, IdProduct, idSeller);
    }

    /**
     * actualizar stock
     * @param stock
     * @param IdProduct
     * @param idSeller
     * @return estado http
     */
    @PatchMapping("/{IdProduct}/stock/{idSeller}")
    public ResponseEntity<?> updateProductStock(@RequestParam("stock") Long stock, @PathVariable Long IdProduct, @PathVariable Long idSeller) {
        productService.updateProductStock(stock, IdProduct, idSeller);
        return ResponseEntity.status(HttpStatus.OK).body("Stock Update");
    }

    /**
     * eliminar producto
     * @param idProduct
     * @param idSeller
     * @return
     */
    @DeleteMapping("/{idProduct}/{idSeller}")
    public ResponseEntity<?> deleteProductId(@PathVariable Long idProduct, @PathVariable Long idSeller) {
        productService.deleteProductId(idProduct, idSeller);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body("removed product");
    }

    /**
     * obtener producto
     * @param Id
     * @return producto
     */
    @GetMapping("/{Id}")
    public ResponseEntity<?> getIdProduct(@PathVariable Long Id) {
        Product findProduct = productService.findId(Id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(findProduct);
    }

    /**
     * listar productos por el vendedor
     * @param idSeller
     * @return
     */
    @GetMapping("/listAll/{idSeller}")
    public ResponseEntity<?> listProductIsSeller(@PathVariable Long idSeller) {
        List<GetProductDTO> products = productService.listProductIsSeller(idSeller);
        return ResponseEntity.status(HttpStatus.OK)
                .body(products);
    }

    /**
     * busqueda de peoductos
     * @param city
     * @param letter
     * @return productos
     */
    @GetMapping("search")
    public ResponseEntity<?> search(@RequestParam String city, @RequestParam String letter) {
        return productService.search(letter,city);
    }

}
