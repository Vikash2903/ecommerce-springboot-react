import axiosClient from './axiosClient';

// ========================================
// GET CURRENT USER CART
// ========================================

export const getCart = async () => {
  const response = await axiosClient.get('/cart');

  return response.data;
};

// ========================================
// ADD PRODUCT TO CART
// ========================================

export const addToCart = async (productId, quantity) => {
  const response = await axiosClient.post('/cart/items', {
    productId,
    quantity,
  });

  return response.data;
};

// ========================================
// UPDATE CART ITEM
// ========================================

export const updateCartItem = async (cartItemId, quantity) => {
  const response = await axiosClient.put(`/cart/items/${cartItemId}`, {
    quantity,
  });

  return response.data;
};

// ========================================
// REMOVE CART ITEM
// ========================================

export const removeCartItem = async (cartItemId) => {
  const response = await axiosClient.delete(`/cart/items/${cartItemId}`);

  return response.data;
};

// ========================================
// CLEAR CART
// ========================================

export const clearCart = async () => {
  const response = await axiosClient.delete('/cart');

  return response.data;
};
