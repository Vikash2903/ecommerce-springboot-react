const CancellationPolicy = ({
  agreedToPolicy,
  policyError,
  onPolicyChange,
}) => {
  return (
    <div className='checkout-section'>
      <h2>2. Cancellation Policy</h2>

      <ul className='policy-list'>
        <li>Orders can be cancelled before they are shipped.</li>

        <li>
          Once an order has been shipped, cancellation may not be available.
        </li>

        <li>
          Refunds for cancelled prepaid orders will be processed according to
          the payment method.
        </li>

        <li>Cancellation rules may depend on the current order status.</li>
      </ul>

      <label className='policy-checkbox'>
        <input
          type='checkbox'
          checked={agreedToPolicy}
          onChange={onPolicyChange}
        />

        <span>I have read and agree to the cancellation policy.</span>
      </label>

      {policyError && <p className='field-error'>{policyError}</p>}
    </div>
  );
};

export default CancellationPolicy;
