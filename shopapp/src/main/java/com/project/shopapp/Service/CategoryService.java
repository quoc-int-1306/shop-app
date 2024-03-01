package com.project.shopapp.Service;

import com.project.shopapp.Dtos.CategoryDTO;
import com.project.shopapp.Models.Category;
import com.project.shopapp.Repository.CategoryRepository;
import com.project.shopapp.Service.impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class CategoryService implements CategoryServiceImpl {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Category createCategory(CategoryDTO category) {
        Category categorySave = Category.builder()
                .name(category.getName())
                .build();
        return categoryRepository.save(categorySave);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional
    public Category updateCategory(Long id, CategoryDTO category) {
        Category categoryExist = getCategoryById(id);
        categoryExist.setName(category.getName());
        categoryRepository.save(categoryExist);
        return categoryExist;
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
//        Category categoryExist = getCategoryById(id);
//        categoryExist.set
        categoryRepository.deleteById(id);
    }
}
