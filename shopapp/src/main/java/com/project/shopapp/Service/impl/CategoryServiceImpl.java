package com.project.shopapp.Service.impl;

import com.project.shopapp.Dtos.CategoryDTO;
import com.project.shopapp.Models.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryServiceImpl {
    Category createCategory(CategoryDTO category);

    Category getCategoryById(Long id);

    List<Category> getAllCategories();

    Category updateCategory(Long id, CategoryDTO category);

    void deleteCategory(Long id);
}
