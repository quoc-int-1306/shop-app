package com.project.shopapp.Controller;

import com.project.shopapp.Dtos.CategoryDTO;
import com.project.shopapp.Models.Category;
import com.project.shopapp.Responses.UpdateCategoryResonse;
import com.project.shopapp.Service.CategoryService;
import com.project.shopapp.Utils.LocalizationUtils;
import com.project.shopapp.Utils.MessageKey;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private LocalizationUtils localizationUtils;

    @GetMapping
    public ResponseEntity<?> getAllCategory(
    ) {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PostMapping("")
    public ResponseEntity<?> insertCategory(
            @Valid @RequestBody CategoryDTO categoryDTO,
            BindingResult result
    ) {
        if(result.hasErrors()) {
               List<String> errorMessages = result.getFieldErrors()
                       .stream()
                       .map(fieldError -> fieldError.getDefaultMessage()).toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok("Inert category successfully" + categoryDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @RequestBody  CategoryDTO categoryDTO
            ) {
        categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok(UpdateCategoryResonse.builder()
                .message(localizationUtils.getLocalizedMessage(MessageKey.UPDATE_CATEGORY_SUCCESSFULLY))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Delete category with id " + id + "successfully");
    }
}
