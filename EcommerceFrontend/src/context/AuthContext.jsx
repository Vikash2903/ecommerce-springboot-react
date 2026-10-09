import { createContext, useContext, useState } from 'react';

import { loginUser, registerUser } from '../api/authApi';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(localStorage.getItem('token'));

  const [user, setUser] = useState(() => {
    const savedUser = localStorage.getItem('user');

    try {
      return savedUser ? JSON.parse(savedUser) : null;
    } catch {
      localStorage.removeItem('user');
      return null;
    }
  });

  const login = async (credentials) => {
    try {
      const response = await loginUser(credentials);

      console.log('Login API response:', response);

      /*
       * Global API response:
       *
       * {
       *   success: true,
       *   message: "Login successful",
       *   data: {
       *      token: "...",
       *      userId: 2,
       *      name: "...",
       *      email: "...",
       *      role: "CUSTOMER"
       *   }
       * }
       */

      if (!response?.success) {
        throw new Error(response?.message || 'Invalid email or password');
      }

      const loginData = response.data;

      if (!loginData?.token) {
        throw new Error('Token was not returned by the server');
      }

      // Save JWT
      localStorage.setItem('token', loginData.token);

      // Save user
      const loggedInUser = {
        userId: loginData.userId,
        name: loginData.name,
        email: loginData.email,
        role: loginData.role,
      };

      localStorage.setItem('user', JSON.stringify(loggedInUser));

      setToken(loginData.token);
      setUser(loggedInUser);

      return loginData;
    } catch (error) {
      console.error('AuthContext login error:', error);

      // IMPORTANT:
      // Do not reload or redirect here.
      throw error;
    }
  };

  const register = async (data) => {
    return await registerUser(data);
  };

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');

    setToken(null);
    setUser(null);
    window.location.href = '/login';
  };

  const isAuthenticated = Boolean(token);

  return (
    <AuthContext.Provider
      value={{
        token,
        user,
        isAuthenticated,
        login,
        register,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  return useContext(AuthContext);
};
