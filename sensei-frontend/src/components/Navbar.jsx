import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useTheme } from '../context/ThemeContext';

function Navbar() {
  const { user, logout } = useAuth();
  const { darkMode, toggleDarkMode } = useTheme();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <nav style={{
      display: 'flex', justifyContent: 'space-between', alignItems: 'center',
      padding: '16px 40px', borderBottom: '1px solid var(--border-color)',
      backgroundColor: 'var(--nav-bg)'
    }}>
      <Link to="/" style={{ fontSize: '24px', fontWeight: 'bold', textDecoration: 'none', color: 'var(--text-primary)' }}>
        SENSEI
      </Link>

      <div style={{ display: 'flex', gap: '20px', alignItems: 'center' }}>
        <Link to="/search" style={{ textDecoration: 'none', color: 'var(--text-secondary)' }}>Find Teachers</Link>

        {/* Dark Mode Toggle */}
        <button onClick={toggleDarkMode} style={{
          padding: '6px 12px', border: '1px solid var(--border-color)', borderRadius: '20px',
          backgroundColor: 'var(--toggle-bg)', cursor: 'pointer', fontSize: '0.85rem',
          color: 'var(--text-secondary)'
        }}>
          {darkMode ? 'Light' : 'Dark'}
        </button>

        {user ? (
          <>
            <Link to="/dashboard" style={{ textDecoration: 'none', color: 'var(--text-secondary)' }}>Dashboard</Link>
            <span style={{ color: 'var(--text-muted)' }}>Hi, {user.name}</span>
            <button onClick={handleLogout} style={{
              padding: '8px 16px', border: '1px solid var(--border-color)', borderRadius: '6px',
              backgroundColor: 'var(--card-bg)', cursor: 'pointer', color: 'var(--text-primary)'
            }}>
              Logout
            </button>
          </>
        ) : (
          <>
            <Link to="/login" style={{ textDecoration: 'none', color: 'var(--text-secondary)' }}>Login</Link>
            <Link to="/signup" style={{
              textDecoration: 'none', color: '#fff', backgroundColor: '#4361ee',
              padding: '8px 20px', borderRadius: '6px'
            }}>
              Sign Up
            </Link>
          </>
        )}
      </div>
    </nav>
  );
}

export default Navbar;
