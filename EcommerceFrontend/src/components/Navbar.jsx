import { Link, NavLink } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useCart } from '../context/CartContext';
import './Navbar.css';

const Navbar = () => {
  const { user, isAuthenticated, logout } = useAuth();
  const { cartItems } = useCart();

  const { cartItemCount } = useCart();

  const cartCount = cartItems?.reduce(
    (total, item) => total + (item.quantity || 1),
    0,
  );

  return (
    <header className='navbar'>
      <div className='navbar-container'>
        {/* LEFT - LOGO */}
        <Link
          to='/'
          className='navbar-logo'
        >
          🏪MyStore
        </Link>

        {/* RIGHT - EVERYTHING */}
        <div className='navbar-menu'>
          <NavLink
            to='/'
            className={({ isActive }) =>
              isActive ? 'nav-link active' : 'nav-link'
            }
          >
            🏠Home
          </NavLink>

          <NavLink
            to='/products'
            className={({ isActive }) =>
              isActive ? 'nav-link active' : 'nav-link'
            }
          >
            🛍️Products
          </NavLink>

          {isAuthenticated && (
            <>
              <NavLink
                to='/cart'
                className={({ isActive }) =>
                  isActive ? 'nav-link active' : 'nav-link'
                }
              >
                🛒Cart
                {cartItemCount > 0 && (
                  <span className='cart-badge'>
                    {cartItemCount > 99 ? '99+' : cartItemCount}
                  </span>
                )}
              </NavLink>

              <NavLink
                to='/orders'
                className={({ isActive }) =>
                  isActive ? 'nav-link active' : 'nav-link'
                }
              >
                📦Orders
              </NavLink>

              <span className='user-name'>👤{user?.name || 'User'}</span>

              <button
                type='button'
                className='logout-button'
                onClick={logout}
              >
                🚪Logout
              </button>
            </>
          )}

          {!isAuthenticated && (
            <div className='auth-buttons'>
              <Link
                to='/login'
                className='auth-button login-button'
              >
                👤Login
              </Link>

              <Link
                to='/register'
                className='auth-button register-button'
              >
                ✨Register
              </Link>
            </div>
          )}
        </div>
      </div>
    </header>
  );
};

export default Navbar;
