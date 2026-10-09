const ShippingForm = ({ address, addressErrors, onChange }) => {
  return (
    <div className='checkout-section'>
      <h2>1. Shipping Address</h2>

      <div className='form-group'>
        <label htmlFor='fullName'>Full Name</label>

        <input
          id='fullName'
          name='fullName'
          type='text'
          value={address.fullName}
          onChange={onChange}
          placeholder='Enter your full name'
        />

        {addressErrors.fullName && (
          <p className='field-error'>{addressErrors.fullName}</p>
        )}
      </div>

      <div className='form-group'>
        <label htmlFor='phone'>Phone Number</label>

        <input
          id='phone'
          name='phone'
          type='tel'
          value={address.phone}
          onChange={onChange}
          placeholder='10-digit mobile number'
          maxLength='10'
        />

        {addressErrors.phone && (
          <p className='field-error'>{addressErrors.phone}</p>
        )}
      </div>

      <div className='form-group'>
        <label htmlFor='addressLine'>Address</label>

        <textarea
          id='addressLine'
          name='addressLine'
          value={address.addressLine}
          onChange={onChange}
          placeholder='House number, street, area'
          rows='3'
        />

        {addressErrors.addressLine && (
          <p className='field-error'>{addressErrors.addressLine}</p>
        )}
      </div>

      <div className='form-row'>
        <div className='form-group'>
          <label htmlFor='city'>City</label>

          <input
            id='city'
            name='city'
            type='text'
            value={address.city}
            onChange={onChange}
            placeholder='City'
          />

          {addressErrors.city && (
            <p className='field-error'>{addressErrors.city}</p>
          )}
        </div>

        <div className='form-group'>
          <label htmlFor='state'>State</label>

          <input
            id='state'
            name='state'
            type='text'
            value={address.state}
            onChange={onChange}
            placeholder='State'
          />

          {addressErrors.state && (
            <p className='field-error'>{addressErrors.state}</p>
          )}
        </div>
      </div>

      <div className='form-group'>
        <label htmlFor='postalCode'>Postal Code</label>

        <input
          id='postalCode'
          name='postalCode'
          type='text'
          value={address.postalCode}
          onChange={onChange}
          placeholder='6-digit postal code'
          maxLength='6'
        />

        {addressErrors.postalCode && (
          <p className='field-error'>{addressErrors.postalCode}</p>
        )}
      </div>
    </div>
  );
};

export default ShippingForm;
