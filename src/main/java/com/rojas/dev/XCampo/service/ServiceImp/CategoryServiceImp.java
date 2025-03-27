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

    /**
     * crea la categoria
     * @param category
     * @return categoria agregada
     */
    @Override
    public Category createNewCategory(Category category) {
       return categoryRepository.save(category);
    }

    /**
     * elimina la categoria
     * @param idCategory
     */
    @Override
    public void deleteCategoryId(Long idCategory) {
        exitsCategoryId(idCategory);
        categoryRepository.deleteById(idCategory);
    }

    /**
     * retorna todas las categorias
     * @return
     */
    @Override
    public List<Category> listAllCategory() {
        return categoryRepository.findAll();
    }

    /**
     * actuaiza la categoria
     * @param categoryId
     * @param name
     * @return
     */
    @Override
    public Category updateCategoryId(Category category) {
        Category findCategoryId = findIdCategory(category.getId_category());
        findCategoryId.setName(category.getName());
        return categoryRepository.save(findCategoryId);
    }

    @Override
    public List<Category> findByNameCategory(String name) {
        return categoryRepository.findByNameCategory(name);
    }

    /**
     * busca la categoria
     * @param id
     * @return
     */
    public Category findIdCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category NOT FOUND with ID: " + id));
    }

    /**
     * verifica si la categoria existe
     * @param categoryId
     */
    public void exitsCategoryId(Long categoryId) {
        if(!categoryRepository.existsById(categoryId)){
            throw new EntityNotFoundException("This category does not exist whit ID: " + categoryId);
        }
    }

}
