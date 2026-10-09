import { Link } from 'react-router-dom';

const OrderSummary = ({ totalItems, cartTotal }) => {
  return (
    <div className='checkout-summary'>
      <h2>Order Summary</h2>

      <div className='summary-row'>
        <span>Items</span>
        <span>{totalItems}</span>
      </div>

      <div className='summary-row'>
        <span>Subtotal</span>
        <span>₹{Number(cartTotal).toFixed(2)}</span>
      </div>

      <div className='summary-row'>
        <span>Shipping</span>
        <span>Free</span>
      </div>

      <div className='summary-divider' />

      <div className='checkout-total'>
        <span>Total</span>
        <strong>₹{Number(cartTotal).toFixed(2)}</strong>
      </div>

      <Link
        to='/cart'
        className='back-to-cart'
      >
        ← Back to Cart
      </Link>
    </div>
  );
};

export default OrderSummary;
