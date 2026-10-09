import { createContext, useContext, useEffect, useState } from 'react';

import {
  getCart,
  addToCart as addToCartApi,
  updateCartItem as updateCartItemApi,
  removeCartItem as removeCartItemApi,
  clearCart as clearCartApi,
} from '../api/cartApi';

import { useAuth } from './AuthContext';

const CartContext = createContext(null);

export const CartProvider = ({ children }) => {
  const { isAuthenticated } = useAuth();

  const [cart, setCart] = useState(null);

  const [loading, setLoading] = useState(false);

  const [error, setError] = useState('');

  // ==========================================
  // CART ITEM COUNT
  // ==========================================

  const cartItemCount =
    cart?.items?.reduce((total, item) => total + item.quantity, 0) || 0;

  // ==========================================
  // LOAD CART
  // ==========================================

  const fetchCart = async () => {
    if (!isAuthenticated) {
      setCart(null);

      return;
    }

    try {
      setLoading(true);

      setError('');

      const response = await getCart();

      if (!response.success) {
        throw new Error(response.message || 'Unable to fetch cart');
      }

      setCart(response.data);
    } catch (error) {
      console.error('Cart loading error:', error);

      const message =
        error.response?.data?.message || error.message || 'Unable to load cart';

      setError(message);
    } finally {
      setLoading(false);
    }
  };

  // ==========================================
  // LOAD CART AFTER LOGIN
  // ==========================================

  useEffect(() => {
    if (isAuthenticated) {
      fetchCart();
    } else {
      setCart(null);
    }
  }, [isAuthenticated]);

  // ==========================================
  // ADD TO CART
  // ==========================================

  const addToCart = async (productId, quantity = 1) => {
    try {
      setError('');

      const response = await addToCartApi(productId, quantity);

      if (!response.success) {
        throw new Error(response.message || 'Unable to add product to cart');
      }

      // Backend returns complete updated cart
      setCart(response.data);

      return response;
    } catch (error) {
      console.error('Add to cart error:', error);

      const message =
        error.response?.data?.message ||
        error.message ||
        'Unable to add product to cart';

      setError(message);

      throw error;
    }
  };

  // ==========================================
  // UPDATE CART ITEM
  // ==========================================

  const updateCartItem = async (cartItemId, quantity) => {
    try {
      setError('');

      const response = await updateCartItemApi(cartItemId, quantity);

      if (!response.success) {
        throw new Error(response.message || 'Unable to update cart item');
      }

      setCart(response.data);

      return response;
    } catch (error) {
      console.error('Update cart error:', error);

      const message =
        error.response?.data?.message ||
        error.message ||
        'Unable to update cart item';

      setError(message);

      throw error;
    }
  };

  // ==========================================
  // REMOVE CART ITEM
  // ==========================================

  const removeCartItem = async (cartItemId) => {
    try {
      setError('');

      const response = await removeCartItemApi(cartItemId);

      if (!response.success) {
        throw new Error(response.message || 'Unable to remove cart item');
      }

      setCart(response.data);

      return response;
    } catch (error) {
      console.error('Remove cart item error:', error);

      const message =
        error.response?.data?.message ||
        error.message ||
        'Unable to remove cart item';

      setError(message);

      throw error;
    }
  };

  // ==========================================
  // CLEAR CART
  // ==========================================

  const clearCart = async () => {
    try {
      setError('');

      const response = await clearCartApi();

      if (!response.success) {
        throw new Error(response.message || 'Unable to clear cart');
      }

      setCart((previousCart) => {
        if (!previousCart) {
          return null;
        }

        return {
          ...previousCart,
          items: [],
          total: 0,
        };
      });

      return response;
    } catch (error) {
      console.error('Clear cart error:', error);

      const message =
        error.response?.data?.message ||
        error.message ||
        'Unable to clear cart';

      setError(message);

      throw error;
    }
  };

  return (
    <CartContext.Provider
      value={{
        cart,
        loading,
        error,
        cartItemCount,
        fetchCart,
        addToCart,
        updateCartItem,
        removeCartItem,
        clearCart,
      }}
    >
      {children}
    </CartContext.Provider>
  );
};

export const useCart = () => {
  return useContext(CartContext);
};
