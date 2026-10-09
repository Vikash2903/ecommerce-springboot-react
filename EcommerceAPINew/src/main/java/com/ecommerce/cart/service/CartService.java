package com.ecommerce.cart.service;

import com.ecommerce.cart.dto.request.AddToCartRequest;
import com.ecommerce.cart.dto.request.UpdateCartItemRequest;
import com.ecommerce.cart.dto.response.CartResponse;

public interface CartService 
{
	CartResponse addToCart(Long userId,AddToCartRequest request);
	
	CartResponse getCart(Long userId);
	
	CartResponse updateCartItem(Long userId, Long cartItemId, UpdateCartItemRequest request);
	
	CartResponse removeCartItem(Long userId, Long cartItemId);
	
	void clearCart(Long userId);
		
	Long getUserIdByEmail(String email);
}
