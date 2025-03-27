package com.rojas.dev.XCampo.service.Interface;

import com.rojas.dev.XCampo.entity.Category;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryService {

    Category createNewCategory(Category category);

    void deleteCategoryId(Long idCategory);

    List<Category> listAllCategory();

    Category updateCategoryId(Category category);

    List<Category> findByNameCategory(String name);

}
