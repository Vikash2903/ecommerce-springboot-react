import { BrowserRouter, Routes, Route } from 'react-router-dom';

import { AuthProvider } from './context/AuthContext';

import Layout from './components/Layout';

import Home from './pages/home/Home';
import Products from './pages/product/Products';
import ProductDetails from './pages/product/ProductDetails';
import Login from './pages/login/Login';
import Register from './pages/register/Register';
import Cart from './pages/cart/Cart';
import Checkout from './pages/checkout/Checkout';
import Orders from './pages/order/Orders';
import OrderDetails from './pages/order/OrderDetails';
import CancelOrder from './pages/order/CancelOrder';
import ProtectedRoute from './components/ProtectedRoute';

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route
            path='/'
            element={<Layout />}
          >
            <Route
              index
              element={<Home />}
            />

            <Route
              path='products'
              element={<Products />}
            />

            <Route
              path='products/:id'
              element={<ProductDetails />}
            />

            <Route
              path='login'
              element={<Login />}
            />

            <Route
              path='register'
              element={<Register />}
            />

            <Route element={<ProtectedRoute />}>
              <Route
                path='cart'
                element={<Cart />}
              />

              <Route
                path='checkout'
                element={<Checkout />}
              />

              <Route
                path='orders'
                element={<Orders />}
              />

              <Route
                path='orders/:id'
                element={<OrderDetails />}
              />
              <Route
                path='orders/:id/cancelled'
                element={<CancelOrder />}
              />
            </Route>
          </Route>
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
