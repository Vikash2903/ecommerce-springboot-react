package com.ecommerce.category.controller;


import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.category.dto.CategoryRequest;
import com.ecommerce.category.dto.CategoryResponse;
import com.ecommerce.category.service.CategoryService;
import com.ecommerce.exception.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController 
{
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService)
    {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>>
    createCategory(
            @Valid
            @RequestBody
            CategoryRequest request) {

        CategoryResponse category =
                categoryService.createCategory(
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "Category created successfully",
                                category
                        )
                );
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() 
    {
    	return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>>
    getCategory(
            @PathVariable Long id) {

        CategoryResponse category =
                categoryService.getCategoryById(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Category fetched successfully",
                        category
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) 
    {
        return ResponseEntity.ok(categoryService.updateCategory(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) 
    {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}