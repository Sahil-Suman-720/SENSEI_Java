import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

function Signup() {
  const [form, setForm] = useState({
    name: '', email: '', password: '', role: 'STUDENT', interests: '', phone: ''
  });
  const [error, setError] = useState('');
  const { signup } = useAuth();
  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      const data = {
        ...form,
        interests: form.role === 'STUDENT'
          ? form.interests.split(',').map(s => s.trim()).filter(Boolean)
          : undefined
      };
      await signup(data);
      navigate('/');
    } catch (err) {
      setError(err.response?.data?.message || 'Signup failed');
    }
  };

  const inputStyle = {
    padding: '12px', borderRadius: '6px',
    border: '1px solid var(--input-border)',
    backgroundColor: 'var(--input-bg)',
    color: 'var(--text-primary)'
  };

  return (
    <div style={{ maxWidth: '400px', margin: '60px auto' }}>
      <h2 style={{ color: 'var(--text-primary)' }}>Sign Up</h2>
      {error && <p style={{ color: '#e74c3c' }}>{error}</p>}

      <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
        <input name="name" placeholder="Full Name" value={form.name}
          onChange={handleChange} required style={inputStyle} />

        <input name="email" type="email" placeholder="Email" value={form.email}
          onChange={handleChange} required style={inputStyle} />

        <input name="password" type="password" placeholder="Password (min 6 chars)" value={form.password}
          onChange={handleChange} required minLength={6} style={inputStyle} />

        <select name="role" value={form.role} onChange={handleChange} style={inputStyle}>
          <option value="STUDENT">I'm a Student</option>
          <option value="TEACHER">I'm a Teacher</option>
        </select>

        {form.role === 'STUDENT' && (
          <input name="interests" placeholder="Interests (comma-separated): Math, Physics, Guitar"
            value={form.interests} onChange={handleChange} style={inputStyle} />
        )}

        <input name="phone" placeholder="Phone (optional)" value={form.phone}
          onChange={handleChange} style={inputStyle} />

        <button type="submit" style={{
          padding: '12px', backgroundColor: '#4361ee', color: '#fff',
          border: 'none', borderRadius: '6px', cursor: 'pointer', fontSize: '1rem'
        }}>
          Create Account
        </button>
      </form>

      <p style={{ marginTop: '20px', textAlign: 'center', color: 'var(--text-muted)' }}>
        Already have an account? <Link to="/login">Login</Link>
      </p>
    </div>
  );
}

export default Signup;
