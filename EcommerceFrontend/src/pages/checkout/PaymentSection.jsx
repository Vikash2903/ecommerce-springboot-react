const PaymentSection = ({
  paymentMethod,
  paymentError,
  orderError,
  paymentSuccess,
  paymentProcessing,
  onPaymentMethodChange,
  onSubmit,
  cartTotal,
  order,
}) => {
  return (
    <div className='checkout-section'>
      <h2>3. Payment Method</h2>

      <div className='payment-options'>
        <label
          className={`payment-option ${
            paymentMethod === 'COD' ? 'selected' : ''
          }`}
        >
          <input
            type='radio'
            name='paymentMethod'
            value='COD'
            checked={paymentMethod === 'COD'}
            onChange={() => onPaymentMethodChange('COD')}
          />

          <div>
            <strong>Cash on Delivery</strong>
            <span>Pay when your order is delivered.</span>
          </div>
        </label>

        <label
          className={`payment-option ${
            paymentMethod === 'CARD' ? 'selected' : ''
          }`}
        >
          <input
            type='radio'
            name='paymentMethod'
            value='CARD'
            checked={paymentMethod === 'CARD'}
            onChange={() => onPaymentMethodChange('CARD')}
          />

          <div>
            <strong>Simulated Card Payment</strong>

            <span>Test payment only. No real money will be charged.</span>
          </div>
        </label>
      </div>

      {paymentError && <p className='field-error'>{paymentError}</p>}

      {orderError && <p className='field-error'>{orderError}</p>}

      {paymentSuccess && (
        <div className='payment-success'>
          <strong>Payment Successful</strong>

          <p>Simulated payment completed successfully.</p>

          <p>Payment method: {paymentMethod}</p>

          <p>Amount: ₹{Number(cartTotal).toFixed(2)}</p>

          {order && <p>Order created successfully.</p>}
        </div>
      )}

      <button
        type='submit'
        className='place-order-button'
        disabled={paymentProcessing}
      >
        {paymentProcessing ? 'Processing Payment...' : 'Simulate Payment'}
      </button>
    </div>
  );
};

export default PaymentSection;
