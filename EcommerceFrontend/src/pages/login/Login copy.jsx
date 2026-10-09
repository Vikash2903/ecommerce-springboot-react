import { useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import './Login.css';

const Login = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { login } = useAuth();

  const [formData, setFormData] = useState({
    email: '',
    password: '',
  });

  const [touched, setTouched] = useState({
    email: false,
    password: false,
  });

  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const successMessage = location.state?.message;

  const isEmailValid = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email);

  const isPasswordValid = formData.password.length >= 6;

  const isFormValid = isEmailValid && isPasswordValid;

  const handleChange = (e) => {
    const { name, value } = e.target;

    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));

    if (error) {
      setError('');
    }
  };

  const handleBlur = (e) => {
    const { name } = e.target;

    setTouched((prev) => ({
      ...prev,
      [name]: true,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!isFormValid || loading) return;

    setError('');
    setLoading(true);

    try {
      await login(formData);
      navigate('/');
    } catch (err) {
      const message =
        err.response?.data?.message ||
        err.message ||
        'Invalid email or password';

      setError(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className='login-page'>
      {/* Decorative Background */}
      <div className='background-circle circle-one'></div>
      <div className='background-circle circle-two'></div>
      <div className='background-circle circle-three'></div>

      <div className='login-wrapper'>
        {/* LEFT SIDE */}
        <div className='login-showcase'>
          <div className='brand-mark'>M</div>

          <h2>
            Welcome to
            <span> My Store</span>
          </h2>

          <p className='showcase-text'>
            Discover amazing products, manage your orders, and enjoy a seamless
            shopping experience.
          </p>

          <div className='showcase-features'>
            <div className='feature'>
              <div className='feature-icon'>✓</div>
              <div>
                <strong>Easy Shopping</strong>
                <p>Find everything you need in one place.</p>
              </div>
            </div>

            <div className='feature'>
              <div className='feature-icon'>✓</div>
              <div>
                <strong>Secure Account</strong>
                <p>Your account and information stay protected.</p>
              </div>
            </div>

            <div className='feature'>
              <div className='feature-icon'>✓</div>
              <div>
                <strong>Fast & Simple</strong>
                <p>A smooth experience from browsing to checkout.</p>
              </div>
            </div>
          </div>
        </div>

        {/* RIGHT SIDE */}
        <div className='login-card'>
          {/* Icon */}
          <div className='login-icon-box'>
            <svg
              className='login-icon'
              viewBox='0 0 24 24'
              fill='none'
              stroke='currentColor'
            >
              <path
                strokeLinecap='round'
                strokeLinejoin='round'
                strokeWidth='2'
                d='M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z'
              />
            </svg>
          </div>

          <div className='login-header'>
            <h1>Welcome Back</h1>

            <p>Sign in to continue to your account</p>
          </div>

          {successMessage && !error && (
            <div className='login-success'>{successMessage}</div>
          )}

          {error && <div className='login-error'>{error}</div>}

          <form
            onSubmit={handleSubmit}
            className='login-form'
            noValidate
          >
            {/* Email */}
            <div className='form-group'>
              <label htmlFor='email'>Email Address</label>

              <div className='input-wrapper'>
                <span className='input-icon'>✉</span>

                <input
                  id='email'
                  name='email'
                  type='email'
                  value={formData.email}
                  onChange={handleChange}
                  onBlur={handleBlur}
                  placeholder='name@company.com'
                  disabled={loading}
                  className={
                    touched.email && !isEmailValid ? 'input-invalid' : ''
                  }
                />
              </div>

              {touched.email && !isEmailValid && (
                <span className='field-error'>
                  Please enter a valid email address
                </span>
              )}
            </div>

            {/* Password */}
            <div className='form-group'>
              <label htmlFor='password'>Password</label>

              <div className='input-wrapper'>
                <span className='input-icon'>🔒</span>

                <input
                  id='password'
                  name='password'
                  type='password'
                  value={formData.password}
                  onChange={handleChange}
                  onBlur={handleBlur}
                  placeholder='••••••••'
                  disabled={loading}
                  className={
                    touched.password && !isPasswordValid ? 'input-invalid' : ''
                  }
                />
              </div>

              {touched.password && !isPasswordValid && (
                <span className='field-error'>
                  Password must be at least 6 characters
                </span>
              )}
            </div>

            {/* Button */}
            <button
              type='submit'
              className='login-button'
              disabled={!isFormValid || loading}
            >
              {loading ? (
                <>
                  <span className='spinner'></span>
                  Signing in...
                </>
              ) : (
                <>
                  Sign In
                  <span className='arrow'>→</span>
                </>
              )}
            </button>
          </form>

          {/* Register */}
          <div className='register-link'>
            <span>Don't have an account?</span>

            <Link to='/register'>Create an account</Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Login;
