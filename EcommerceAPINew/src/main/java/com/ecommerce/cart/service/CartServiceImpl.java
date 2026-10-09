package com.ecommerce.cart.service;

import com.ecommerce.cart.dto.request.AddToCartRequest;
import com.ecommerce.cart.dto.request.UpdateCartItemRequest;
import com.ecommerce.cart.dto.response.CartItemResponse;
import com.ecommerce.cart.dto.response.CartResponse;
import com.ecommerce.cart.entity.Cart;
import com.ecommerce.cart.entity.CartItem;
import com.ecommerce.cart.repository.CartItemRepository;
import com.ecommerce.cart.repository.CartRepository;
import com.ecommerce.exception.InsufficientStockException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.repository.ProductRepository;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartServiceImpl implements CartService
{
    private static final Logger log = LoggerFactory.getLogger(CartServiceImpl.class);

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductRepository productRepository, UserRepository userRepository) 
    {
        this.cartRepository = cartRepository;
        
        this.cartItemRepository = cartItemRepository;
        
        this.productRepository = productRepository;
        
        this.userRepository = userRepository;
    }

    @Transactional
    public CartResponse addToCart(Long userId, AddToCartRequest request) 
    {
        log.info("Adding product {} to cart for user {}", request.getProductId(), userId);
       
        User user = findUser(userId);
        Product product = productRepository.findById(request.getProductId()).orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id: "+ request.getProductId()));
        
        Cart cart = getOrCreateCart(user);
        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId()).orElse(null);
        int newQuantity;
        if (cartItem == null) 
        {
            newQuantity = request.getQuantity();
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
        } 
        else 
        {
            newQuantity = cartItem.getQuantity() + request.getQuantity();
        }

        validateStock(product, newQuantity);
        cartItem.setQuantity(newQuantity);
        cartItemRepository.save(cartItem);

        log.info("Product {} added to cart. Quantity: {}", product.getId(), newQuantity);

        return buildCartResponse(cart);
    }

    @Transactional(readOnly = true)
    public CartResponse getCart(Long userId) 
    {
    	log.info("Fetching cart for user {}", userId);
        User user = findUser(userId);
        Cart cart = cartRepository.findByUserId(user.getId())
                        .orElseGet(() -> {
                            Cart newCart = new Cart();
                            newCart.setUser(user);
                            return cartRepository.save(newCart);
                        });
        return buildCartResponse(cart);
    }

    @Transactional
    public CartResponse updateCartItem(Long userId, Long cartItemId, UpdateCartItemRequest request) 
    {
        log.info("Updating cart item {} for user {}", cartItemId, userId);
        Cart cart = getExistingCart(userId);
        CartItem cartItem =cartItemRepository.findByIdAndCartId(cartItemId, cart.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: "+ cartItemId));
        validateStock(cartItem.getProduct(),request.getQuantity());
        cartItem.setQuantity(request.getQuantity());
        cartItemRepository.save(cartItem);
        return buildCartResponse(cart);
    }

    @Transactional
    public CartResponse removeCartItem(Long userId,Long cartItemId) 
    {
        log.info("Removing cart item {} for user {}", cartItemId, userId);
        Cart cart = getExistingCart(userId);
        CartItem cartItem = cartItemRepository.findByIdAndCartId(cartItemId, cart.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: "+ cartItemId));
        //cartItemRepository.delete(cartItem);
        cart.getItems().remove(cartItem);
        log.info("Cart item {} removed", cartItemId);
        return buildCartResponse(cart);
    }

    @Transactional
    public void clearCart(Long userId) 
    {
        log.info("Clearing cart for user {}", userId);
        Cart cart = getExistingCart(userId);
        cart.getItems().clear();
        cartRepository.save(cart);
        log.info("Cart cleared for user {}", userId);
    }

    private Cart getOrCreateCart(User user) 
    {
        return cartRepository.findByUserId(user.getId()).orElseGet(() ->{
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
       });
    }
    private Cart getExistingCart(Long userId) 
    {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() ->new ResourceNotFoundException("Cart not found for user: "+ userId));
    }

    private User findUser(Long userId) 
    {
        return userRepository.findById(userId)
        		.orElseThrow(() ->new ResourceNotFoundException("User not found with id: "+ userId));
    }

    @Transactional(readOnly = true)
    public Long getUserIdByEmail(String email) 
    {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: "+ email)).getId();
    }
    
    private void validateStock(Product product, int requestedQuantity) 
    {
        if (requestedQuantity > product.getStock()) 
        {
            throw new InsufficientStockException("Insufficient stock for product: "+ product.getName()+ ". Available stock: "+product.getStock());
        }
    }

    private CartResponse buildCartResponse(Cart cart) 
    {
        List<CartItemResponse> items = cart.getItems().stream().map(this::mapToCartItemResponse).toList();
        BigDecimal total = items.stream().map(CartItemResponse::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponse(cart.getId(), cart.getUser().getId(), items, total);
    }

    private CartItemResponse mapToCartItemResponse( CartItem item) 
    {
        BigDecimal subtotal = item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

        return new CartItemResponse(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getProduct().getPrice(),
                item.getQuantity(),
                subtotal
        );
    }
}