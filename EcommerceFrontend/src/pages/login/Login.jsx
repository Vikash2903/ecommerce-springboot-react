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
  const [showPassword, setShowPassword] = useState(false);

  const successMessage = location.state?.message;

  const isEmailValid = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email);
  const isPasswordValid = formData.password.length >= 6;
  const isFormValid = isEmailValid && isPasswordValid;

  const handleChange = (event) => {
    const { name, value } = event.target;

    setFormData((previous) => ({
      ...previous,
      [name]: value,
    }));

    if (error) {
      setError('');
    }
  };

  const handleBlur = (event) => {
    const { name } = event.target;

    setTouched((previous) => ({
      ...previous,
      [name]: true,
    }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    setTouched({
      email: true,
      password: true,
    });

    if (!isFormValid || loading) {
      return;
    }

    setError('');
    setLoading(true);

    try {
      await login(formData);
      navigate('/');
    } catch (err) {
      console.error('Login error:', err);

      const message =
        err.response?.data?.message ||
        err.message ||
        'Invalid email or password.';

      setError(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className='login-page'>
      <div className='login-card'>
        <div className='login-header'>
          <div className='login-icon'>
            <svg
              viewBox='0 0 24 24'
              fill='none'
              stroke='currentColor'
              aria-hidden='true'
            >
              <path
                strokeLinecap='round'
                strokeLinejoin='round'
                strokeWidth='2'
                d='M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z'
              />
            </svg>
          </div>

          <h1>Welcome Back</h1>

          <p>Sign in to continue to your account.</p>
        </div>

        {successMessage && !error && (
          <div
            className='login-success'
            role='status'
          >
            {successMessage}
          </div>
        )}

        {error && (
          <div
            className='login-error'
            role='alert'
          >
            <span className='error-icon'>!</span>
            <span>{error}</span>
          </div>
        )}

        <form
          onSubmit={handleSubmit}
          className='login-form'
          noValidate
        >
          <div className='form-group'>
            <label htmlFor='email'>Email Address</label>

            <div className='input-wrapper'>
              <span className='input-icon'></span>

              <input
                id='email'
                name='email'
                type='email'
                value={formData.email}
                onChange={handleChange}
                onBlur={handleBlur}
                placeholder='@Enter your email'
                autoComplete='email'
                disabled={loading}
                required
                className={
                  touched.email && !isEmailValid ? 'input-invalid' : ''
                }
              />
            </div>

            {touched.email && !isEmailValid && (
              <span className='field-error'>
                Please enter a valid email address.
              </span>
            )}
          </div>

          <div className='form-group'>
            <label htmlFor='password'>Password</label>

            <div className='input-wrapper'>
              <span className='input-icon'></span>

              <input
                id='password'
                name='password'
                type={showPassword ? 'text' : 'password'}
                value={formData.password}
                onChange={handleChange}
                onBlur={handleBlur}
                placeholder='•Enter your password'
                autoComplete='current-password'
                disabled={loading}
                required
                className={
                  touched.password && !isPasswordValid ? 'input-invalid' : ''
                }
              />

              <button
                type='button'
                className='password-toggle'
                onClick={() => setShowPassword((previous) => !previous)}
                disabled={loading}
                aria-label={showPassword ? 'Hide password' : 'Show password'}
              >
                {showPassword ? 'Hide' : 'Show'}
              </button>
            </div>

            {touched.password && !isPasswordValid && (
              <span className='field-error'>
                Password must be at least 6 characters.
              </span>
            )}
          </div>

          <button
            type='submit'
            className='login-submit'
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
                <span className='button-arrow'>→</span>
              </>
            )}
          </button>
        </form>

        <div className='register-link'>
          <span>Don't have an account?</span>
          <Link to='/register'>Create an account</Link>
        </div>
      </div>
    </div>
  );
};

export default Login;
