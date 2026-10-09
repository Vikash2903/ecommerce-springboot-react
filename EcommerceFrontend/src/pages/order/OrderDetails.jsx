import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';

import { getOrderById, cancelOrder } from '../../api/orderApi';

import './OrderDetails.css';

const OrderDetails = () => {
  const { id } = useParams();

  const [order, setOrder] = useState(null);

  const [loading, setLoading] = useState(true);

  const [error, setError] = useState('');

  const [cancelling, setCancelling] = useState(false);

  // ==========================================
  // LOAD ORDER
  // ==========================================

  useEffect(() => {
    fetchOrder();
  }, [id]);

  const fetchOrder = async () => {
    try {
      setLoading(true);
      setError('');

      const response = await getOrderById(id);

      console.log('Order details API response:', response);

      if (!response.success) {
        throw new Error(response.message || 'Unable to fetch order');
      }

      setOrder(response.data);
    } catch (error) {
      console.error('Order details error:', error);

      const message =
        error.response?.data?.message ||
        error.message ||
        'Unable to load order';

      setError(message);
    } finally {
      setLoading(false);
    }
  };

  // ==========================================
  // CANCEL ORDER
  // ==========================================

  const handleCancelOrder = async () => {
    const confirmed = window.confirm(
      'Are you sure you want to cancel this order?',
    );

    if (!confirmed) {
      return;
    }

    try {
      setCancelling(true);
      setError('');

      const response = await cancelOrder(id);

      console.log('Cancel order response:', response);

      if (!response.success) {
        throw new Error(response.message || 'Unable to cancel order');
      }

      setOrder(response.data);
    } catch (error) {
      console.error('Cancel order error:', error);

      const message =
        error.response?.data?.message ||
        error.message ||
        'Unable to cancel order';

      setError(message);
    } finally {
      setCancelling(false);
    }
  };

  // ==========================================
  // LOADING
  // ==========================================

  if (loading) {
    return (
      <div className='order-details-page'>
        <div className='order-details-status'>Loading order...</div>
      </div>
    );
  }

  // ==========================================
  // ERROR
  // ==========================================

  if (error && !order) {
    return (
      <div className='order-details-page'>
        <div className='order-details-error'>
          <h2>Unable to load order</h2>

          <p>{error}</p>

          <Link to='/orders'>Back to My Orders</Link>
        </div>
      </div>
    );
  }

  if (!order) {
    return null;
  }

  // ==========================================
  // ORDER DETAILS
  // ==========================================

  return (
    <div className='order-details-page'>
      <div className='order-details-container'>
        <Link
          to='/orders'
          className='back-to-orders'
        >
          ← Back to My Orders
        </Link>

        <div className='order-details-header'>
          <div>
            <h1>Order #{order.id}</h1>

            <p>
              Placed on{' '}
              {order.createdAt
                ? new Date(order.createdAt).toLocaleString()
                : ''}
            </p>
          </div>

          <span className='order-details-status-badge'>{order.status}</span>
        </div>

        {error && <div className='order-details-error-message'>{error}</div>}

        {/* =====================================
            SHIPPING ADDRESS
            ===================================== */}

        <div className='order-details-card'>
          <h2>Shipping Address</h2>

          <p>{order.shippingAddress}</p>
        </div>

        {/* =====================================
            ORDER ITEMS
            ===================================== */}

        <div className='order-details-card'>
          <h2>Order Items</h2>

          <div className='order-items'>
            {order.items?.map((item) => (
              <div
                className='order-item'
                key={item.id}
              >
                <div className='order-item-info'>
                  <h3>{item.productName}</h3>

                  <p>
                    ₹{item.unitPrice} × {item.quantity}
                  </p>
                </div>

                <strong>₹{item.subtotal}</strong>
              </div>
            ))}
          </div>
        </div>

        {/* =====================================
            ORDER SUMMARY
            ===================================== */}

        <div className='order-details-card order-summary-card'>
          <div className='order-summary-row'>
            <span>Total</span>

            <strong>₹{order.totalAmount}</strong>
          </div>
        </div>

        {/* =====================================
            CANCEL ORDER
            ===================================== */}

        {order.status === 'PENDING' && (
          <div className='cancel-order-section'>
            <button
              type='button'
              className='cancel-order-button'
              onClick={handleCancelOrder}
              disabled={cancelling}
            >
              {cancelling ? 'Cancelling...' : 'Cancel Order'}
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

export default OrderDetails;
