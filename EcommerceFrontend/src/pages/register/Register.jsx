import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import './Register.css';

const Register = () => {
  const navigate = useNavigate();
  const { register } = useAuth();

  const [formData, setFormData] = useState({
    name: '',
    email: '',
    password: '',
  });

  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [showPassword, setShowPassword] = useState(false);

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

  const handleSubmit = async (event) => {
    event.preventDefault();
    event.stopPropagation();

    if (loading) {
      return;
    }

    setError('');

    // Simple frontend validation
    if (!formData.name.trim()) {
      setError('Please enter your full name.');
      return;
    }

    if (!formData.email.trim()) {
      setError('Please enter your email address.');
      return;
    }

    if (!formData.password) {
      setError('Please create a password.');
      return;
    }

    if (formData.password.length < 6) {
      setError('Password must be at least 6 characters.');
      return;
    }

    setLoading(true);

    try {
      await register(formData);

      navigate('/login', {
        state: {
          message: 'Account created successfully! Please sign in to continue.',
        },
      });
    } catch (error) {
      console.error('Register error:', error);

      const message =
        error.response?.data?.message ||
        error.message ||
        'Failed to create account. Please try again.';

      setError(message);
    } finally {
      setLoading(false);
    }
  };

  // Password strength
  const getPasswordStrength = () => {
    const password = formData.password;

    if (!password) {
      return {
        label: '',
        className: '',
      };
    }

    if (password.length < 6) {
      return {
        label: 'Weak password',
        className: 'weak',
      };
    }

    if (
      password.length >= 8 &&
      /[A-Z]/.test(password) &&
      /[0-9]/.test(password)
    ) {
      return {
        label: 'Strong password',
        className: 'strong',
      };
    }

    return {
      label: 'Good password',
      className: 'medium',
    };
  };

  const passwordStrength = getPasswordStrength();

  return (
    <div className='register-page'>
      <div className='register-card'>
        {/* Header */}
        <div className='register-header'>
          <div className='register-icon'>+</div>

          <h1>Create Account</h1>

          <p>Create your account and start shopping.</p>
        </div>

        {/* Error */}
        {error && (
          <div
            className='register-error'
            role='alert'
          >
            <span className='error-icon'>!</span>
            <span>{error}</span>
          </div>
        )}

        {/* Form */}
        <form
          onSubmit={handleSubmit}
          className='register-form'
          noValidate
        >
          {/* Name */}
          <div className='form-group'>
            <label htmlFor='name'>Full Name</label>

            <div className='input-wrapper'>
              <span className='input-icon'></span>

              <input
                id='name'
                name='name'
                type='text'
                value={formData.name}
                onChange={handleChange}
                placeholder='👤Enter your full name'
                autoComplete='name'
                disabled={loading}
                required
              />
            </div>
          </div>

          {/* Email */}
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
                placeholder='@Enter your email'
                autoComplete='email'
                disabled={loading}
                required
              />
            </div>
          </div>

          {/* Password */}
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
                placeholder='.Create a password'
                autoComplete='new-password'
                disabled={loading}
                required
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

            {/* Password strength */}
            {passwordStrength.label && (
              <div className='password-strength'>
                <div className='strength-bars'>
                  <span className={passwordStrength.className}></span>
                  <span
                    className={
                      passwordStrength.className === 'medium' ||
                      passwordStrength.className === 'strong'
                        ? passwordStrength.className
                        : ''
                    }
                  ></span>
                  <span
                    className={
                      passwordStrength.className === 'strong' ? 'strong' : ''
                    }
                  ></span>
                </div>

                <small className={passwordStrength.className}>
                  {passwordStrength.label}
                </small>
              </div>
            )}
          </div>

          {/* Submit */}
          <button
            type='submit'
            className='register-submit'
            disabled={loading}
          >
            {loading ? (
              <>
                <span className='spinner'></span>
                Creating account...
              </>
            ) : (
              <>
                Create Account
                <span className='button-arrow'>→</span>
              </>
            )}
          </button>
        </form>

        {/* Login */}
        <div className='login-link'>
          <span>Already have an account?</span>

          <Link to='/login'>Sign In</Link>
        </div>
      </div>
    </div>
  );
};

export default Register;
