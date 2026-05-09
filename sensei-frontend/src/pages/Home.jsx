import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useEffect, useState } from 'react';
import { recommendationApi, bookingApi } from '../services/api';

function Home() {
  const { user } = useAuth();
  const [recommendations, setRecommendations] = useState([]);
  const [teacherBookings, setTeacherBookings] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (user && user.role === 'STUDENT') {
      setLoading(true);
      recommendationApi.get(12)
        .then(res => setRecommendations(res.data.data || []))
        .catch(() => {})
        .finally(() => setLoading(false));
    } else if (user && user.role === 'TEACHER') {
      setLoading(true);
      bookingApi.getTeacherBookings()
        .then(res => setTeacherBookings(res.data.data || []))
        .catch(() => {})
        .finally(() => setLoading(false));
    }
  }, [user]);

  return (
    <div>
      {/* Hero section — only show when NOT logged in */}
      {!user && (
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

      {/* TEACHER HOME — upcoming bookings, stats, calendar */}
      {user && user.role === 'TEACHER' && (
        <section style={{ marginTop: '20px' }}>
          <h1 style={{ fontSize: '2rem', color: 'var(--text-primary)' }}>Welcome back, {user.name}!</h1>
          <p style={{ color: 'var(--text-muted)', marginTop: '4px' }}>Here's your teaching overview</p>

          {/* Stats Row */}
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '16px', marginTop: '24px' }}>
            <div style={{ padding: '20px', backgroundColor: 'var(--card-bg)', borderRadius: '10px', border: '1px solid var(--border-color)', textAlign: 'center' }}>
              <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>Total Sessions</p>
              <p style={{ fontSize: '2rem', fontWeight: 'bold', color: 'var(--text-primary)' }}>{teacherBookings.length}</p>
            </div>
            <div style={{ padding: '20px', backgroundColor: 'var(--card-bg)', borderRadius: '10px', border: '1px solid var(--border-color)', textAlign: 'center' }}>
              <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>Upcoming</p>
              <p style={{ fontSize: '2rem', fontWeight: 'bold', color: 'var(--accent)' }}>
                {teacherBookings.filter(b => b.status === 'CONFIRMED').length}
              </p>
            </div>
            <div style={{ padding: '20px', backgroundColor: 'var(--card-bg)', borderRadius: '10px', border: '1px solid var(--border-color)', textAlign: 'center' }}>
              <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>Completed</p>
              <p style={{ fontSize: '2rem', fontWeight: 'bold', color: '#27ae60' }}>
                {teacherBookings.filter(b => b.status === 'COMPLETED').length}
              </p>
            </div>
          </div>

          {/* Upcoming Bookings */}
          <div style={{ marginTop: '30px' }}>
            <h2 style={{ color: 'var(--text-primary)', marginBottom: '16px' }}>Upcoming Sessions</h2>
            {teacherBookings.filter(b => b.status === 'CONFIRMED').length === 0 && (
              <p style={{ color: 'var(--text-muted)' }}>No upcoming sessions scheduled.</p>
            )}
            <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
              {teacherBookings
                .filter(b => b.status === 'CONFIRMED')
                .sort((a, b) => new Date(a.bookingDate) - new Date(b.bookingDate))
                .map(booking => (
                  <div key={booking.id} style={{
                    padding: '16px', backgroundColor: 'var(--card-bg)', borderRadius: '8px',
                    border: '1px solid var(--border-color)', display: 'flex',
                    justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '8px'
                  }}>
                    <div>
                      <strong style={{ color: 'var(--text-primary)' }}>{booking.studentName}</strong>
                      <p style={{ color: 'var(--text-muted)', margin: '4px 0 0', fontSize: '0.9rem' }}>
                        {booking.bookingDate} | {booking.startTime} - {booking.endTime}
                      </p>
                    </div>
                    <span style={{ padding: '4px 12px', borderRadius: '20px', fontSize: '0.85rem', backgroundColor: '#d4edda', color: '#155724' }}>
                      Confirmed
                    </span>
                  </div>
                ))}
            </div>
          </div>

          {/* Recent Sessions Calendar View */}
          <div style={{ marginTop: '30px' }}>
            <h2 style={{ color: 'var(--text-primary)', marginBottom: '16px' }}>Recent Sessions</h2>
            {teacherBookings.filter(b => b.status === 'COMPLETED').length === 0 && (
              <p style={{ color: 'var(--text-muted)' }}>No completed sessions yet.</p>
            )}
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(250px, 1fr))', gap: '12px' }}>
              {teacherBookings
                .filter(b => b.status === 'COMPLETED')
                .slice(0, 12)
                .map(booking => (
                  <div key={booking.id} style={{
                    padding: '12px 16px', backgroundColor: 'var(--card-bg)', borderRadius: '8px',
                    border: '1px solid var(--border-color)'
                  }}>
                    <p style={{ color: 'var(--text-primary)', fontWeight: '500' }}>{booking.studentName}</p>
                    <p style={{ color: 'var(--text-muted)', fontSize: '0.85rem' }}>
                      {booking.bookingDate} | {booking.startTime}-{booking.endTime}
                    </p>
                    <p style={{ color: 'var(--text-muted)', fontSize: '0.85rem' }}>₹{booking.amount}</p>
                  </div>
                ))}
            </div>
          </div>
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
