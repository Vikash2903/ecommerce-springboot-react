package com.ecommerce.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import com.ecommerce.exception.ApiResponse;
import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.service.ProductService;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product API", description = "APIs for managing products")
public class ProductController 
{
    private final ProductService productService;

    public ProductController(ProductService productService) 
    {
        this.productService = productService;
    }

    @PostMapping
    @Operation(summary = "Create product")
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@Valid @RequestBody ProductRequest request) 
    {
        ProductResponse product = productService.createProduct(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Product created successfully", product));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(@PathVariable Long id) 
    {
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success("Product fetched successfully", product));
    }

    @GetMapping
    @Operation(summary = "Get all products")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts() 
    {

        List<ProductResponse> products = productService.getAllProducts();

        return ResponseEntity.ok(ApiResponse.success("Products fetched successfully", products));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product")
    public ResponseEntity<ApiResponse<ProductResponse>>
    updateProduct(
            @PathVariable Long id,

            @Valid
            @RequestBody
            ProductRequest request) {

        ProductResponse product =
                productService.updateProduct(
                        id,
                        request
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Product updated successfully",
                        product
                )
        );
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete product"
    )
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id) {

        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }
}