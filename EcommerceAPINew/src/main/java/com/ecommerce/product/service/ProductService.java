package com.ecommerce.product.service;

import java.util.List;

import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;

public interface ProductService 
{
	ProductResponse createProduct(ProductRequest request);
	ProductResponse getProductById(Long id);
	ProductResponse updateProduct(Long id,ProductRequest request);
	void deleteProduct(Long id);
	List<ProductResponse> getAllProducts();
}
