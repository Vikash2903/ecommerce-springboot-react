package com.ecommerce.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.category.entity.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> 
{
    boolean existsByNameIgnoreCase(String name);
    Optional<Category> findByNameIgnoreCase(String name);
}