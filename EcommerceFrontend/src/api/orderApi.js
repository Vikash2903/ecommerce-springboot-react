import axiosClient from './axiosClient';

// ==========================================
// CREATE ORDER
// ==========================================

export const createOrder = async (data) => {
  const response = await axiosClient.post('/orders', data);

  return response.data;
};

// ==========================================
// GET MY ORDERS
// ==========================================

export const getMyOrders = async (page = 0, size = 10) => {
  const response = await axiosClient.get('/orders', {
    params: {
      page,
      size,
    },
  });

  return response.data;
};

// ==========================================
// GET ORDER BY ID
// ==========================================

export const getOrderById = async (orderId) => {
  const response = await axiosClient.get(`/orders/${orderId}`);

  return response.data;
};

// ==========================================
// CANCEL ORDER
// ==========================================

export const cancelOrder = async (orderId) => {
  const response = await axiosClient.patch(`/orders/${orderId}/cancel`);

  return response.data;
};
