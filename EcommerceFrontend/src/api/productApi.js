import axiosClient from './axiosClient';

// Get all products
export const getProducts = async () => {
  const response = await axiosClient.get('/products');

  return response.data;
};

// Get product by ID
export const getProductById = async (id) => {
  const response = await axiosClient.get(`/products/${id}`);

  return response.data;
};
