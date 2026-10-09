import { Link, useParams } from 'react-router-dom';
import './CancelOrder.css';

const CancelOrder = () => {
  const { id } = useParams();

  return (
    <div className='cancel-order-page'>
      <div className='cancel-order-container'>
        <div className='cancel-order-icon'>❌</div>

        <h1>Order #{id} Cancelled</h1>

        <p>Your order has been successfully cancelled.</p>

        <div className='cancel-order-actions'>
          <Link
            to='/orders'
            className='back-to-orders-button'
          >
            Back to My Orders
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

export default CancelOrder;
