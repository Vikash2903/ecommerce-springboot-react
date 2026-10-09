package com.ecommerce.product.service;

import com.ecommerce.category.entity.Category;
import com.ecommerce.category.repository.CategoryRepository;
import com.ecommerce.exception.DuplicateResourceException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService 
{
    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) 
    {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest request) 
    {
        log.info("Creating product: {}", request.getName());

        if (productRepository.existsByNameIgnoreCase(request.getName().trim())) 
        {
            log.warn("Product already exists: {}", request.getName());
            throw new DuplicateResourceException("Product already exists: "+ request.getName());
        }
        Category category = categoryRepository.findById(request.getCategoryId())
                        .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        Product product = new Product();
        product.setName(request.getName().trim());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        log.info("Product created successfully with id: {}", savedProduct.getId());

        return mapToResponse(savedProduct);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) 
    {
        log.info("Fetching product with id: {}", id);

        Product product = productRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return mapToResponse(product);
    }
    
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() 
    {
    	List<Product> products = productRepository.findAll();
		return products.stream().map(product->mapToResponse(product)).collect(Collectors.toList());
	}
    
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) 
    {
        log.info("Updating product with id: {}",id);

        Product product = productRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        Category category = categoryRepository.findById(request.getCategoryId())
                        .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: "+ request.getCategoryId()));

        product.setName(request.getName().trim());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(category);

        Product updatedProduct = productRepository.save(product);

        log.info("Product updated successfully: {}", id);
        return mapToResponse(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) 
    {
        log.info("Deleting product with id: {}", id);

        Product product = productRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: "+ id));

        productRepository.delete(product);
        log.info("Product deleted successfully: {}", id);
    }

    private ProductResponse mapToResponse(Product product) 
    {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategory().getId(),
                product.getCategory().getName()
        );
    }
}