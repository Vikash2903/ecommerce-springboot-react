package com.ecommerce.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.cart.entity.CartItem;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> 
{
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);

    Optional<CartItem> findByIdAndCartId(Long id, Long cartId);
}