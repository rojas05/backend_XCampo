package com.rojas.dev.XCampo.service.ServiceImp;

import com.rojas.dev.XCampo.entity.Category;
import com.rojas.dev.XCampo.exception.EntityNotFoundException;
import com.rojas.dev.XCampo.repository.CategoryRepository;
import com.rojas.dev.XCampo.service.Interface.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImp implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category createNewCategory(Category category) {
        Long id = category.getId_category();
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategoryId(Long idCategory) {
        exitsCategoryId(idCategory);
        categoryRepository.deleteById(idCategory);
    }

    @Override
    public List<Category> listAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Category updateCategoryId(Long categoryId, String name) {
        Category category = findIdCategory(categoryId);
        category.setName(name);
        return categoryRepository.save(category);
    }

    public Category findIdCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category NOT FOUND with ID: " + id));
    }


    public void exitsCategoryId(Long categoryId) {
        if(!categoryRepository.existsById(categoryId)){
            throw new EntityNotFoundException("No existe esta categoria");
        }
    }

}
