import { Link } from "react-router-dom";
import "./Home.css";

const Home = () => {
  return (
    <div className="home-page">
      <section className="hero-section">
        <div className="hero-content">
          <p className="hero-label">WELCOME TO OUR STORE</p>

          <h1>
            Shop Smart.
            <br />
            Live Better.
          </h1>

          <p className="hero-description">
            Discover quality products at great prices. Find everything you
            need, all in one place.
          </p>

          <div className="hero-actions">
            <Link to="/products" className="hero-primary-button">
              Shop Now
            </Link>

            <Link to="/products" className="hero-secondary-button">
              Explore Products
            </Link>
          </div>
        </div>
      </section>

      <section className="features-section">
        <div className="features-container">
          <div className="feature-card">
            <div className="feature-icon">🚚</div>
            <h3>Fast Delivery</h3>
            <p>Get your orders delivered quickly and safely.</p>
          </div>

          <div className="feature-card">
            <div className="feature-icon">🔒</div>
            <h3>Secure Shopping</h3>
            <p>Your information and orders are protected.</p>
          </div>

          <div className="feature-card">
            <div className="feature-icon">💳</div>
            <h3>Easy Payment</h3>
            <p>Choose from convenient payment options.</p>
          </div>

          <div className="feature-card">
            <div className="feature-icon">⭐</div>
            <h3>Quality Products</h3>
            <p>Shop products selected for quality and value.</p>
          </div>
        </div>
      </section>

      <section className="home-cta-section">
        <div className="home-cta">
          <h2>Ready to Start Shopping?</h2>

          <p>
            Explore our collection and find something you'll love.
          </p>

          <Link to="/products" className="home-cta-button">
            Browse Products
          </Link>
        </div>
      </section>
    </div>
  );
};

export default Home;