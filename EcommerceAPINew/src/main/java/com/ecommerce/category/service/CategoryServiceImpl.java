package com.ecommerce.category.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.category.dto.CategoryRequest;
import com.ecommerce.category.dto.CategoryResponse;
import com.ecommerce.category.entity.Category;
import com.ecommerce.category.repository.CategoryRepository;
import com.ecommerce.exception.DuplicateResourceException;
import com.ecommerce.exception.ResourceNotFoundException;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService
{
    private static final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) 
    {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) 
    {
        log.info("Creating category: {}", request.getName());
        if (categoryRepository.existsByNameIgnoreCase(request.getName())) 
        {
            log.warn("Category already exists: {}",request.getName());
            throw new DuplicateResourceException("Category already exists: " + request.getName());
        }

        Category category = new Category();
        category.setName(request.getName().trim());

        Category savedCategory = categoryRepository.save(category);

        log.info("Category created successfully with id: {}", savedCategory.getId());
        return mapToResponse(savedCategory);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() 
    {
        log.info("Fetching all categories");
        return categoryRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) 
    {
        log.info("Fetching category with id: {}", id);

        Category category = categoryRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: "+ id));
        return mapToResponse(category);
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) 
    {
        log.info("Updating category with id: {}", id);
        Category category = categoryRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: "+ id));
        String newName = request.getName().trim();

        if (!category.getName().equalsIgnoreCase(newName) && categoryRepository.existsByNameIgnoreCase(newName)) 
        {
            throw new DuplicateResourceException("Category already exists: " + newName);
        }
        category.setName(newName);
        Category updatedCategory = categoryRepository.save(category);
        
        log.info("Category updated successfully: {}", id);

        return mapToResponse(updatedCategory);
    }

    @Transactional
    public void deleteCategory(Long id)
    {
        log.info("Deleting category with id: {}", id);
        Category category = categoryRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        categoryRepository.delete(category);
        log.info("Category deleted successfully: {}", id);
    }

    private CategoryResponse mapToResponse(Category category) 
    {
        return new CategoryResponse(category.getId(), category.getName());
    }
}