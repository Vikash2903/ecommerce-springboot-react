package com.ecommerce.category.service;

import java.util.List;

import com.ecommerce.category.dto.CategoryRequest;
import com.ecommerce.category.dto.CategoryResponse;

import jakarta.validation.Valid;

public interface CategoryService
{
	CategoryResponse createCategory(@Valid CategoryRequest request);

	List<CategoryResponse> getAllCategories();
	
	CategoryResponse getCategoryById(Long id);
	
	CategoryResponse updateCategory(Long id, CategoryRequest request) ;
	
	void deleteCategory(Long id);
}
