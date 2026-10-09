import { Link } from 'react-router-dom';

import { useCart } from '../../context/CartContext';

import './Cart.css';

const Cart = () => {
  const { cart, loading, error, updateCartItem, removeCartItem, clearCart } =
    useCart();

  // ==========================================
  // UPDATE QUANTITY
  // ==========================================

  const handleQuantityChange = async (cartItem, newQuantity) => {
    if (newQuantity < 1) {
      return;
    }

    try {
      await updateCartItem(cartItem.id, newQuantity);
    } catch (error) {
      console.error('Update cart error:', error);
    }
  };

  // ==========================================
  // REMOVE ITEM
  // ==========================================

  const handleRemoveItem = async (cartItemId) => {
    try {
      await removeCartItem(cartItemId);
    } catch (error) {
      console.error('Remove cart item error:', error);
    }
  };

  // ==========================================
  // CLEAR CART
  // ==========================================

  const handleClearCart = async () => {
    if (!cart?.items?.length) {
      return;
    }

    const confirmed = window.confirm(
      'Are you sure you want to clear your cart?',
    );

    if (!confirmed) {
      return;
    }

    try {
      await clearCart();
    } catch (error) {
      console.error('Clear cart error:', error);
    }
  };

  // ==========================================
  // LOADING
  // ==========================================

  if (loading) {
    return (
      <div className='cart-page'>
        <div className='cart-status'>Loading cart...</div>
      </div>
    );
  }

  // ==========================================
  // ERROR
  // ==========================================

  if (error && !cart) {
    return (
      <div className='cart-page'>
        <div className='cart-error-page'>
          <h2>Unable to load cart</h2>

          <p>{error}</p>
        </div>
      </div>
    );
  }

  // ==========================================
  // EMPTY CART
  // ==========================================

  if (!cart || !cart.items || cart.items.length === 0) {
    return (
      <div className='cart-page'>
        <div className='empty-cart'>
          <div className='empty-cart-icon'>🛒</div>

          <h1>Your Cart is Empty</h1>

          <p>You haven't added any products to your cart yet.</p>

          <Link
            to='/products'
            className='continue-shopping'
          >
            Continue Shopping
          </Link>
        </div>
      </div>
    );
  }

  // ==========================================
  // TOTAL ITEM QUANTITY
  // ==========================================

  const totalItems = cart.items.reduce(
    (total, item) => total + item.quantity,
    0,
  );

  // ==========================================
  // CART
  // ==========================================

  return (
    <div className='cart-page'>
      <div className='cart-container'>
        {/* HEADER */}

        <div className='cart-header'>
          <div>
            <h1>Shopping Cart</h1>

            <p>
              {totalItems} {totalItems === 1 ? 'item' : 'items'} in your cart
            </p>
          </div>

          <button
            type='button'
            className='clear-cart-button'
            onClick={handleClearCart}
          >
            Clear Cart
          </button>
        </div>

        {/* ERROR */}

        {error && <div className='cart-error-message'>{error}</div>}

        <div className='cart-layout'>
          {/* CART ITEMS */}

          <div className='cart-items'>
            {cart.items.map((item) => (
              <div
                className='cart-item'
                key={item.id}
              >
                {/* PRODUCT INFORMATION */}

                <div className='cart-item-info'>
                  <h2>{item.productName}</h2>

                  <p className='cart-item-price'>
                    ₹{Number(item.price).toFixed(2)}
                  </p>
                </div>

                {/* ACTIONS */}

                <div className='cart-item-actions'>
                  {/* QUANTITY */}

                  <div className='quantity-control'>
                    <button
                      type='button'
                      onClick={() =>
                        handleQuantityChange(item, item.quantity - 1)
                      }
                      disabled={item.quantity <= 1}
                    >
                      −
                    </button>

                    <span>{item.quantity}</span>

                    <button
                      type='button'
                      onClick={() =>
                        handleQuantityChange(item, item.quantity + 1)
                      }
                    >
                      +
                    </button>
                  </div>

                  {/* SUBTOTAL */}

                  <div className='cart-item-subtotal'>
                    ₹{Number(item.subtotal).toFixed(2)}
                  </div>

                  {/* REMOVE */}

                  <button
                    type='button'
                    className='remove-item-button'
                    onClick={() => handleRemoveItem(item.id)}
                  >
                    Remove
                  </button>
                </div>
              </div>
            ))}
          </div>

          {/* ORDER SUMMARY */}

          <div className='cart-summary'>
            <h2>Order Summary</h2>

            <div className='summary-row'>
              <span>Items</span>

              <span>{totalItems}</span>
            </div>

            <div className='summary-divider' />

            <div className='summary-total'>
              <span>Total</span>

              <strong>₹{Number(cart.total).toFixed(2)}</strong>
            </div>

            <Link
              to='/checkout'
              className='checkout-button'
            >
              Proceed to Checkout
            </Link>

            <Link
              to='/products'
              className='continue-shopping-link'
            >
              Continue Shopping
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Cart;
