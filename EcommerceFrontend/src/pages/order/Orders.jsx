import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';

import { getMyOrders } from '../../api/orderApi';

import './Orders.css';

const Orders = () => {
  const [orders, setOrders] = useState([]);

  const [loading, setLoading] = useState(true);

  const [error, setError] = useState('');

  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = async () => {
    try {
      setLoading(true);
      setError('');

      const response = await getMyOrders();

      console.log('Orders API response:', response);

      if (!response.success) {
        throw new Error(response.message || 'Unable to fetch orders');
      }

      setOrders(response.data?.content || []);
    } catch (error) {
      console.error('Orders error:', error);

      const message =
        error.response?.data?.message ||
        error.message ||
        'Unable to load orders';

      setError(message);
    } finally {
      setLoading(false);
    }
  };

  // ==========================================
  // LOADING
  // ==========================================

  if (loading) {
    return (
      <div className='orders-page'>
        <div className='orders-status'>Loading orders...</div>
      </div>
    );
  }

  // ==========================================
  // ERROR
  // ==========================================

  if (error) {
    return (
      <div className='orders-page'>
        <div className='orders-error'>
          <h2>Unable to load orders</h2>

          <p>{error}</p>

          <button
            type='button'
            onClick={fetchOrders}
          >
            Try Again
          </button>
        </div>
      </div>
    );
  }

  // ==========================================
  // NO ORDERS
  // ==========================================

  if (orders.length === 0) {
    return (
      <div className='orders-page'>
        <div className='empty-orders'>
          <h1>My Orders</h1>

          <p>You have not placed any orders yet.</p>

          <Link to='/products'>Continue Shopping</Link>
        </div>
      </div>
    );
  }

  // ==========================================
  // ORDERS
  // ==========================================

  return (
    <div className='orders-page'>
      <div className='orders-container'>
        <div className='orders-header'>
          <h1>My Orders</h1>

          <p>View your order history</p>
        </div>

        <div className='orders-list'>
          {orders.map((order) => (
            <div
              className='order-card'
              key={order.id}
            >
              <div className='order-card-header'>
                <div>
                  <h2>Order #{order.id}</h2>

                  <p>
                    {order.createdAt
                      ? new Date(order.createdAt).toLocaleString()
                      : ''}
                  </p>
                </div>

                <span className='order-status'>{order.status}</span>
              </div>

              <div className='order-card-body'>
                <div>
                  <span>Total</span>

                  <strong>₹{order.totalAmount}</strong>
                </div>

                <div>
                  <span>Shipping Address</span>

                  <strong>{order.shippingAddress}</strong>
                </div>
              </div>

              <div className='order-card-footer'>
                <Link
                  to={`/orders/${order.id}`}
                  className='view-order-button'
                >
                  View Order
                </Link>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default Orders;
