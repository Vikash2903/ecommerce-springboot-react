import axios from 'axios';

const axiosClient = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,

  headers: {
    'Content-Type': 'application/json',
  },
});

// ================================
// REQUEST INTERCEPTOR
// ================================

axiosClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');

    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
  },

  (error) => {
    return Promise.reject(error);
  },
);

// ================================
// RESPONSE INTERCEPTOR
// ================================

axiosClient.interceptors.response.use(
  (response) => {
    return response;
  },

  (error) => {
    const status = error.response?.status;

    const requestUrl = error.config?.url;

    // =====================================
    // 401 - UNAUTHORIZED
    // =====================================

    if (status === 401) {
      /*
       * IMPORTANT:
       *
       * Do NOT redirect when the login API
       * itself returns 401.
       */

      const isLoginRequest = requestUrl?.includes('/auth/login');

      if (!isLoginRequest) {
        localStorage.removeItem('token');

        localStorage.removeItem('user');

        window.location.href = '/login';
      }
    }

    return Promise.reject(error);
  },
);

export default axiosClient;
