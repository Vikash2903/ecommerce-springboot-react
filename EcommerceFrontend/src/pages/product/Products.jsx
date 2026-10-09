import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';

import { getProducts } from '../../api/productApi';
import { useCart } from '../../context/CartContext';

import './Products.css';

const Products = () => {
  const { addToCart } = useCart();

  const [products, setProducts] = useState([]);

  const [loading, setLoading] = useState(true);

  const [error, setError] = useState('');

  const [addingProductId, setAddingProductId] = useState(null);

  const [cartMessage, setCartMessage] = useState({});

  // =====================================
  // LOAD PRODUCTS
  // =====================================

  useEffect(() => {
    fetchProducts();
  }, []);

  const fetchProducts = async () => {
    try {
      setLoading(true);
      setError('');

      const response = await getProducts();

      console.log('Products API response:', response);

      if (!response.success) {
        throw new Error(response.message || 'Unable to fetch products');
      }

      setProducts(response.data || []);
    } catch (error) {
      console.error('Products error:', error);

      const message =
        error.response?.data?.message ||
        error.message ||
        'Unable to load products';

      setError(message);
    } finally {
      setLoading(false);
    }
  };

  // =====================================
  // ADD TO CART
  // =====================================

  const handleAddToCart = async (productId) => {
    try {
      setAddingProductId(productId);

      setCartMessage((previous) => ({
        ...previous,
        [productId]: '',
      }));

      const response = await addToCart(productId, 1);

      console.log('Add to cart response:', response);

      setCartMessage((previous) => ({
        ...previous,
        [productId]: response.message || 'Product added to cart successfully',
      }));
    } catch (error) {
      console.error('Add to cart error:', error);

      const message =
        error.response?.data?.message ||
        error.message ||
        'Unable to add product to cart';

      setCartMessage((previous) => ({
        ...previous,
        [productId]: message,
      }));
    } finally {
      setAddingProductId(null);
    }
  };

  // =====================================
  // LOADING
  // =====================================

  if (loading) {
    return (
      <div className='products-page'>
        <div className='products-status'>Loading products...</div>
      </div>
    );
  }

  // =====================================
  // ERROR
  // =====================================

  if (error) {
    return (
      <div className='products-page'>
        <div className='products-error'>
          <h2>Unable to load products</h2>

          <p>{error}</p>

          <button
            type='button'
            onClick={fetchProducts}
          >
            Try Again
          </button>
        </div>
      </div>
    );
  }

  // =====================================
  // PRODUCTS
  // =====================================

  return (
    <div className='products-page'>
      <div className='products-container'>
        {/* HEADER */}

        <div className='products-header'>
          <div>
            <h1>Products</h1>

            <p>Browse our latest products</p>
          </div>
        </div>

        {/* EMPTY */}

        {products.length === 0 ? (
          <div className='empty-products'>
            <h2>No products found</h2>

            <p>There are currently no products available.</p>
          </div>
        ) : (
          <div className='products-grid'>
            {products.map((product) => (
              <div
                className='product-card'
                key={product.id}
              >
                <div className='product-content'>
                  <h2>{product.name}</h2>

                  <p className='product-description'>{product.description}</p>

                  <div className='product-footer'>
                    <span className='product-price'>₹{product.price}</span>

                    <span
                      className={`product-stock ${product.stock <= 0 ? 'out-of-stock' : ''}`}
                    >
                      {product.stock > 0
                        ? `In Stock: ${product.stock}`
                        : 'Out of Stock'}
                    </span>
                  </div>

                  {/* MESSAGE */}

                  {cartMessage[product.id] && (
                    <div className='cart-message'>
                      {cartMessage[product.id]}
                    </div>
                  )}

                  {/* BUTTONS */}

                  <div className='product-actions'>
                    <Link
                      to={`/products/${product.id}`}
                      className='view-product'
                    >
                      View Product
                    </Link>

                    <button
                      type='button'
                      className='add-cart-button'
                      onClick={() => handleAddToCart(product.id)}
                      disabled={
                        product.stock <= 0 || addingProductId === product.id
                      }
                    >
                      {addingProductId === product.id
                        ? 'Adding...'
                        : product.stock <= 0
                          ? 'Out of Stock'
                          : 'Add to Cart'}
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default Products;
