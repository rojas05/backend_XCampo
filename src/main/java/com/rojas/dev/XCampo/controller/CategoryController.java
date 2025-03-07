package com.rojas.dev.XCampo.controller;

import com.rojas.dev.XCampo.entity.Category;
import com.rojas.dev.XCampo.service.Interface.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<?> createNewCategory(@RequestBody Category category) {
        Category categoryEntity = categoryService.createNewCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryEntity);
    }

    @DeleteMapping("/{idCategory}")
    public ResponseEntity<?> deleteIdCategory(@PathVariable Long idCategory) {
        categoryService.deleteCategoryId(idCategory);
        return ResponseEntity.status(HttpStatus.OK).body("Delete category with ID: " + idCategory);
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateNameCategory(@RequestBody Category category) {
        var findCategory = categoryService.updateCategoryId(category);
        return ResponseEntity.status(HttpStatus.OK).body(findCategory);
    }

    @GetMapping("/{findName}")
    public ResponseEntity<?> findNameCategory(@PathVariable String findName) {
        var category = categoryService.findByNameCategory(findName);
        return ResponseEntity.status(HttpStatus.FOUND).body(category);
    }

    @GetMapping
    public ResponseEntity<?> listAllCategory() {
        var category = categoryService.listAllCategory();
        return ResponseEntity.status(HttpStatus.FOUND).body(category);
    }

}
