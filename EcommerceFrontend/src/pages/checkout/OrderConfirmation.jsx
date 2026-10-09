import { Link } from 'react-router-dom';

const OrderConfirmation = ({ order, paymentMethod, orderError }) => {
  return (
    <div className='checkout-page'>
      <div className='order-success'>
        <div className='order-success-icon'>✓</div>

        <h1>Order Placed Successfully!</h1>

        <p>Thank you for your order.</p>

        <div className='order-success-details'>
          <p>
            <strong>Order ID:</strong> {order.id}
          </p>

          <p>
            <strong>Payment Method:</strong> {paymentMethod}
          </p>

          <p>
            <strong>Total:</strong> ₹{Number(order.totalAmount || 0).toFixed(2)}
          </p>
        </div>

        {orderError && <p className='field-error'>{orderError}</p>}

        <div className='order-success-actions'>
          <Link
            to='/orders'
            className='view-orders-button'
          >
            View My Orders
          </Link>

          <Link
            to='/products'
            className='continue-shopping-button'
          >
            Continue Shopping
          </Link>
        </div>
      </div>
    </div>
  );
};

export default OrderConfirmation;
