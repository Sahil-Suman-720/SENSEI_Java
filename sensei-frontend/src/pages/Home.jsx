import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useEffect, useState } from 'react';
import { recommendationApi } from '../services/api';

function Home() {
  const { user } = useAuth();
  const [recommendations, setRecommendations] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (user && user.role === 'STUDENT') {
      setLoading(true);
      recommendationApi.get(12)
        .then(res => setRecommendations(res.data.data || []))
        .catch(() => {})
        .finally(() => setLoading(false));
    }
  }, [user]);

  return (
    <div>
      {/* Hero section — only show when not logged in or teacher */}
      {(!user || user.role === 'TEACHER') && (
        <section style={{ textAlign: 'center', padding: '60px 20px' }}>
          <h1 style={{ fontSize: '3rem', marginBottom: '16px', color: 'var(--text-primary)' }}>Find Your Perfect Teacher</h1>
          <p style={{ fontSize: '1.2rem', color: 'var(--text-muted)', marginBottom: '32px' }}>
            Book 1-on-1 sessions with expert teachers in any subject
          </p>
          <Link to="/search" style={{
            display: 'inline-block', padding: '14px 32px', backgroundColor: '#4361ee',
            color: '#fff', borderRadius: '8px', textDecoration: 'none', fontSize: '1.1rem'
          }}>
            Browse Teachers
          </Link>
        </section>
      )}

      {/* Personalized welcome + recommendations for logged-in students */}
      {user && user.role === 'STUDENT' && (
        <section style={{ marginTop: '20px' }}>
          <div style={{ marginBottom: '30px' }}>
            <h1 style={{ fontSize: '2rem', color: 'var(--text-primary)' }}>Welcome back, {user.name}!</h1>
            <p style={{ color: 'var(--text-muted)', fontSize: '1.1rem', marginTop: '8px' }}>
              Here are teachers recommended based on your interests
            </p>
          </div>

          {loading && <p style={{ color: 'var(--text-muted)' }}>Loading recommendations...</p>}

          {!loading && recommendations.length > 0 && (
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: '20px' }}>
              {recommendations.map(teacher => (
                <Link to={`/teachers/${teacher.id}`} key={teacher.id} style={{ textDecoration: 'none', color: 'inherit' }}>
                  <div style={{
                    border: '1px solid var(--border-color)', borderRadius: '12px', padding: '20px',
                    backgroundColor: 'var(--card-bg)', transition: 'transform 0.2s, box-shadow 0.2s'
                  }}>
                    <h3 style={{ color: 'var(--text-primary)', marginBottom: '8px' }}>{teacher.name}</h3>
                    <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>{teacher.subjects?.join(', ')}</p>
                    {teacher.city && <p style={{ color: 'var(--text-muted)', fontSize: '0.85rem', marginTop: '4px' }}>{teacher.city}</p>}
                    <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '12px', alignItems: 'center' }}>
                      <span style={{ color: 'var(--text-secondary)' }}>⭐ {teacher.avgRating?.toFixed(1) || 'New'} ({teacher.totalReviews || 0})</span>
                      <span style={{ fontWeight: 'bold', color: 'var(--accent)' }}>₹{teacher.hourlyRate}/hr</span>
                    </div>
                    <div style={{ marginTop: '8px', color: 'var(--text-muted)', fontSize: '0.85rem' }}>
                      {teacher.experienceYears} years exp | {teacher.totalBookings} sessions
                    </div>
                  </div>
                </Link>
              ))}
            </div>
          )}

          {!loading && recommendations.length === 0 && (
            <div style={{ textAlign: 'center', padding: '40px', color: 'var(--text-muted)' }}>
              <p>No recommendations yet. <Link to="/search">Browse all teachers</Link></p>
            </div>
          )}

          <div style={{ textAlign: 'center', marginTop: '30px' }}>
            <Link to="/search" style={{
              display: 'inline-block', padding: '12px 28px', backgroundColor: 'var(--card-bg)',
              color: 'var(--accent)', borderRadius: '8px', textDecoration: 'none',
              border: '1px solid var(--accent)', fontSize: '1rem'
            }}>
              Browse All Teachers
            </Link>
          </div>
        </section>
      )}
    </div>
  );
}

export default Home;
