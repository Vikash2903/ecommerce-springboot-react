import { useState } from 'react';
import { Link } from 'react-router-dom';

import { useCart } from '../../context/CartContext';
import { createOrder } from '../../api/orderApi';

import ShippingForm from './ShippingForm';
import CancellationPolicy from './CancellationPolicy';
import PaymentSection from './PaymentSection';
import OrderSummary from './OrderSummary';
import OrderConfirmation from './OrderConfirmation';

import './Checkout.css';

const Checkout = () => {
  const { cart, loading, error } = useCart();

  const [address, setAddress] = useState({
    fullName: '',
    phone: '',
    addressLine: '',
    city: '',
    state: '',
    postalCode: '',
  });

  const [addressErrors, setAddressErrors] = useState({});
  const [agreedToPolicy, setAgreedToPolicy] = useState(false);
  const [policyError, setPolicyError] = useState('');
  const [paymentMethod, setPaymentMethod] = useState('');
  const [paymentError, setPaymentError] = useState('');
  const [paymentProcessing, setPaymentProcessing] = useState(false);
  const [paymentSuccess, setPaymentSuccess] = useState(false);
  const [order, setOrder] = useState(null);
  const [orderError, setOrderError] = useState('');

  const handleChange = (event) => {
    const { name, value } = event.target;

    setAddress((previous) => ({
      ...previous,
      [name]: value,
    }));

    setAddressErrors((previous) => ({
      ...previous,
      [name]: '',
    }));
  };

  const validateAddress = () => {
    const errors = {};

    if (!address.fullName.trim()) {
      errors.fullName = 'Full name is required';
    }

    if (!address.phone.trim()) {
      errors.phone = 'Phone number is required';
    } else if (!/^[6-9]\d{9}$/.test(address.phone)) {
      errors.phone = 'Enter a valid 10-digit Indian mobile number';
    }

    if (!address.addressLine.trim()) {
      errors.addressLine = 'Address is required';
    }

    if (!address.city.trim()) {
      errors.city = 'City is required';
    }

    if (!address.state.trim()) {
      errors.state = 'State is required';
    }

    if (!address.postalCode.trim()) {
      errors.postalCode = 'Postal code is required';
    } else if (!/^\d{6}$/.test(address.postalCode)) {
      errors.postalCode = 'Enter a valid 6-digit postal code';
    }

    setAddressErrors(errors);

    return Object.keys(errors).length === 0;
  };

  const handlePolicyChange = (event) => {
    const checked = event.target.checked;

    setAgreedToPolicy(checked);

    if (checked) {
      setPolicyError('');
    }
  };

  const handlePaymentMethodChange = (method) => {
    setPaymentMethod(method);
    setPaymentError('');
    setPaymentSuccess(false);
    setOrderError('');
  };

  const handlePayment = (event) => {
    event.preventDefault();

    setPaymentError('');
    setPaymentSuccess(false);
    setOrderError('');

    const isAddressValid = validateAddress();

    if (!isAddressValid) {
      return;
    }

    if (!agreedToPolicy) {
      setPolicyError('Please agree to the cancellation policy');
      return;
    }

    setPolicyError('');

    if (!paymentMethod) {
      setPaymentError('Please select a payment method');
      return;
    }

    setPaymentProcessing(true);

    setTimeout(async () => {
      try {
        const shippingAddress = [
          address.fullName,
          address.phone,
          address.addressLine,
          address.city,
          address.state,
          address.postalCode,
        ]
          .filter(Boolean)
          .join(', ');

        const orderResponse = await createOrder({
          shippingAddress,
        });
        console.log('Create order response:', orderResponse);
        if (!orderResponse.success) {
          throw new Error(orderResponse.message || 'Unable to create order');
        }
        setOrder(orderResponse.data);

        setPaymentSuccess(true);
      } catch (error) {
        console.error('Create order error:', error);

        const message =
          error.response?.data?.message ||
          error.message ||
          'Unable to create order';

        setOrderError(message);
        setPaymentSuccess(false);
      } finally {
        setPaymentProcessing(false);
      }
    }, 2000);
  };

  if (loading) {
    return (
      <div className='checkout-page'>
        <div className='checkout-status'>Loading checkout...</div>
      </div>
    );
  }

  if (error && !cart) {
    return (
      <div className='checkout-page'>
        <div className='checkout-error'>
          <h2>Unable to load checkout</h2>

          <p>{error}</p>

          <Link to='/cart'>Back to Cart</Link>
        </div>
      </div>
    );
  }

  if (!cart || !cart.items || cart.items.length === 0) {
    return (
      <div className='checkout-page'>
        <div className='empty-checkout'>
          <h1>Your Cart is Empty</h1>

          <p>Please add products to your cart before proceeding to checkout.</p>

          <Link
            to='/products'
            className='back-to-products'
          >
            Continue Shopping
          </Link>
        </div>
      </div>
    );
  }

  {
    /*ORDER SUMMARY*/
  }
  if (order) {
    return (
      <OrderConfirmation
        order={order}
        paymentMethod={paymentMethod}
        orderError={orderError}
      />
    );
  }

  const totalItems = cart.items.reduce(
    (total, item) => total + item.quantity,
    0,
  );

  return (
    <div className='checkout-page'>
      <div className='checkout-container'>
        <div className='checkout-header'>
          <h1>Checkout</h1>

          <p>Review your order and complete payment.</p>
        </div>

        <div className='checkout-layout'>
          <div className='checkout-items'>
            <form onSubmit={handlePayment}>
              {/*SHIPPING FORM*/}
              <ShippingForm
                address={address}
                addressErrors={addressErrors}
                onChange={handleChange}
              />

              {/*CANCELLLATION POLICY*/}
              <CancellationPolicy
                agreedToPolicy={agreedToPolicy}
                policyError={policyError}
                onPolicyChange={handlePolicyChange}
              />

              {/*PAYMENT SECTION*/}
              <PaymentSection
                paymentMethod={paymentMethod}
                paymentError={paymentError}
                orderError={orderError}
                paymentSuccess={paymentSuccess}
                paymentProcessing={paymentProcessing}
                onPaymentMethodChange={handlePaymentMethodChange}
                onSubmit={handlePayment}
                cartTotal={cart.total}
                order={order}
              />
            </form>
          </div>

          {/*CHECKOUT SUMMARY*/}
          <OrderSummary
            totalItems={totalItems}
            cartTotal={cart.total}
          />
        </div>
      </div>
    </div>
  );
};

export default Checkout;
