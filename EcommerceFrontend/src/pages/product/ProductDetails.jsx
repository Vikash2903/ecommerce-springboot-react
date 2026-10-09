import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';

import { getProductById } from '../../api/productApi';
import { useCart } from '../../context/CartContext';

import './ProductDetails.css';

const ProductDetails = () => {
  const { addToCart } = useCart();

  const { id } = useParams();

  const [product, setProduct] = useState(null);

  const [quantity, setQuantity] = useState(1);

  const [loading, setLoading] = useState(true);

  const [addingToCart, setAddingToCart] = useState(false);

  const [error, setError] = useState('');

  const [successMessage, setSuccessMessage] = useState('');

  // =====================================
  // LOAD PRODUCT
  // =====================================

  useEffect(() => {
    fetchProduct();
  }, [id]);

  const fetchProduct = async () => {
    try {
      setLoading(true);

      setError('');

      const response = await getProductById(id);

      console.log('Product details response:', response);

      if (!response.success) {
        throw new Error(response.message || 'Unable to load product');
      }

      setProduct(response.data);
    } catch (error) {
      console.error('Product details error:', error);

      const message =
        error.response?.data?.message ||
        error.message ||
        'Unable to load product';

      setError(message);
    } finally {
      setLoading(false);
    }
  };

  // =====================================
  // QUANTITY
  // =====================================

  const increaseQuantity = () => {
    if (product && quantity < product.stock) {
      setQuantity((previous) => previous + 1);
    }
  };

  const decreaseQuantity = () => {
    if (quantity > 1) {
      setQuantity((previous) => previous - 1);
    }
  };

  // =====================================
  // ADD TO CART
  // =====================================

  const handleAddToCart = async () => {
    if (!product) {
      return;
    }

    if (product.stock <= 0) {
      setError('This product is out of stock');

      return;
    }

    try {
      setAddingToCart(true);

      setError('');

      setSuccessMessage('');

      const response = await addToCart(product.id, quantity);

      console.log('Add to cart response:', response);

      if (!response.success) {
        throw new Error(response.message || 'Unable to add product to cart');
      }

      setSuccessMessage(
        response.message || 'Product added to cart successfully',
      );
    } catch (error) {
      console.error('Add to cart error:', error);

      const message =
        error.response?.data?.message ||
        error.message ||
        'Unable to add product to cart';

      setError(message);
    } finally {
      setAddingToCart(false);
    }
  };

  // =====================================
  // LOADING
  // =====================================

  if (loading) {
    return (
      <div className='product-details-page'>
        <div className='details-status'>Loading product...</div>
      </div>
    );
  }

  // =====================================
  // ERROR
  // =====================================

  if (error && !product) {
    return (
      <div className='product-details-page'>
        <div className='details-error'>
          <h2>Unable to load product</h2>

          <p>{error}</p>

          <Link to='/products'>Back to Products</Link>
        </div>
      </div>
    );
  }

  if (!product) {
    return null;
  }

  // =====================================
  // PRODUCT PAGE
  // =====================================

  return (
    <div className='product-details-page'>
      <div className='product-details-container'>
        <Link
          to='/products'
          className='back-link'
        >
          ← Back to Products
        </Link>

        <div className='product-details-card'>
          <div className='product-details-content'>
            <div className='product-details-info'>
              <h1>{product.name}</h1>

              <p className='product-details-description'>
                {product.description}
              </p>

              <div className='product-details-price'>₹{product.price}</div>

              <div className='product-details-stock'>
                {product.stock > 0 ? (
                  <span className='in-stock'>In Stock ({product.stock})</span>
                ) : (
                  <span className='out-of-stock'>Out of Stock</span>
                )}
              </div>

              {product.stock > 0 && (
                <div className='quantity-section'>
                  <label>Quantity</label>

                  <div className='quantity-control'>
                    <button
                      type='button'
                      onClick={decreaseQuantity}
                      disabled={quantity <= 1}
                    >
                      −
                    </button>

                    <span>{quantity}</span>

                    <button
                      type='button'
                      onClick={increaseQuantity}
                      disabled={quantity >= product.stock}
                    >
                      +
                    </button>
                  </div>
                </div>
              )}

              {error && <div className='cart-error'>{error}</div>}

              {successMessage && (
                <div className='cart-success'>{successMessage}</div>
              )}

              <button
                type='button'
                className='add-cart-button'
                onClick={handleAddToCart}
                disabled={addingToCart || product.stock <= 0}
              >
                {addingToCart
                  ? 'Adding...'
                  : product.stock <= 0
                    ? 'Out of Stock'
                    : 'Add to Cart'}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProductDetails;
