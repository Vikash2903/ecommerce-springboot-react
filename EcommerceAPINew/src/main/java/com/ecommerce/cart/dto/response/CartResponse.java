package com.ecommerce.cart.dto.response;

import java.math.BigDecimal;
import java.util.List;

public class CartResponse
{
    private Long cartId;
    private Long userId;
    private List<CartItemResponse> items;
    private BigDecimal total;

    public CartResponse(
            Long cartId,
            Long userId,
            List<CartItemResponse> items,
            BigDecimal total) {

        this.cartId = cartId;
        this.userId = userId;
        this.items = items;
        this.total = total;
    }

    public Long getCartId() {
        return cartId;
    }

    public Long getUserId() {
        return userId;
    }

    public List<CartItemResponse> getItems() {
        return items;
    }

    public BigDecimal getTotal() {
        return total;
    }
}