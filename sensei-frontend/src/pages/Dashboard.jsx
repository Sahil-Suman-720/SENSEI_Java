import { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { bookingApi, reviewApi } from '../services/api';

function Dashboard() {
  const { user } = useAuth();
  const [bookings, setBookings] = useState([]);
  const [reviewForm, setReviewForm] = useState({ bookingId: null, rating: 5, comment: '' });
  const [message, setMessage] = useState('');

  useEffect(() => {
    if (user?.role === 'STUDENT') {
      bookingApi.getMyBookings().then(res => setBookings(res.data.data || [])).catch(() => {});
    } else if (user?.role === 'TEACHER') {
      bookingApi.getTeacherBookings().then(res => setBookings(res.data.data || [])).catch(() => {});
    }
  }, [user]);

  const handleReview = async (e) => {
    e.preventDefault();
    try {
      await reviewApi.create(reviewForm);
      setMessage('Review submitted!');
      setReviewForm({ bookingId: null, rating: 5, comment: '' });
    } catch (err) {
      setMessage(err.response?.data?.message || 'Failed to submit review');
    }
  };

  if (!user) return <p style={{ color: 'var(--text-muted)' }}>Loading...</p>;

  const inputStyle = {
    padding: '12px', borderRadius: '6px',
    border: '1px solid var(--input-border)',
    backgroundColor: 'var(--input-bg)',
    color: 'var(--text-primary)'
  };

  return (
    <div>
      <h1 style={{ color: 'var(--text-primary)' }}>Dashboard</h1>
      <p style={{ color: 'var(--text-muted)', marginBottom: '24px' }}>Welcome, {user.name} ({user.role})</p>

      <h2 style={{ color: 'var(--text-primary)', marginBottom: '16px' }}>
        My {user.role === 'STUDENT' ? 'Bookings' : 'Sessions'}
      </h2>

      {bookings.length === 0 && <p style={{ color: 'var(--text-muted)' }}>No bookings yet.</p>}

      <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
        {bookings.map(booking => (
          <div key={booking.id} style={{
            border: '1px solid var(--border-color)', borderRadius: '8px', padding: '16px',
            display: 'flex', justifyContent: 'space-between', alignItems: 'center',
            backgroundColor: 'var(--card-bg)', flexWrap: 'wrap', gap: '12px'
          }}>
            <div>
              <strong style={{ color: 'var(--text-primary)' }}>
                {user.role === 'STUDENT' ? booking.teacherName : booking.studentName}
              </strong>
              <p style={{ margin: '4px 0', color: 'var(--text-muted)' }}>
                {booking.bookingDate} | {booking.startTime} - {booking.endTime}
              </p>
              <p style={{ margin: 0, color: 'var(--text-muted)' }}>₹{booking.amount}</p>
            </div>

            <div style={{ textAlign: 'right' }}>
              <span style={{
                padding: '4px 12px', borderRadius: '20px', fontSize: '0.85rem',
                backgroundColor:
                  booking.status === 'CONFIRMED' ? '#d4edda' :
                  booking.status === 'COMPLETED' ? '#cce5ff' :
                  booking.status === 'CANCELLED' ? '#f8d7da' : '#fff3cd',
                color:
                  booking.status === 'CONFIRMED' ? '#155724' :
                  booking.status === 'COMPLETED' ? '#004085' :
                  booking.status === 'CANCELLED' ? '#721c24' : '#856404'
              }}>
                {booking.status}
              </span>

              {user.role === 'STUDENT' && booking.status === 'COMPLETED' && (
                <button onClick={() => setReviewForm({ ...reviewForm, bookingId: booking.id })}
                  style={{
                    display: 'block', marginTop: '8px', color: 'var(--accent)',
                    background: 'none', border: 'none', cursor: 'pointer'
                  }}>
                  Write Review
                </button>
              )}
            </div>
          </div>
        ))}
      </div>

      {/* Review Form */}
      {reviewForm.bookingId && (
        <div style={{
          marginTop: '30px', border: '1px solid var(--border-color)',
          borderRadius: '8px', padding: '20px', backgroundColor: 'var(--card-bg)'
        }}>
          <h3 style={{ color: 'var(--text-primary)' }}>Write a Review</h3>
          {message && <p style={{ color: message.includes('submitted') ? '#27ae60' : '#e74c3c' }}>{message}</p>}
          <form onSubmit={handleReview} style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
            <div>
              <label style={{ color: 'var(--text-secondary)' }}>Rating: </label>
              <select value={reviewForm.rating} onChange={e => setReviewForm({ ...reviewForm, rating: parseInt(e.target.value) })} style={inputStyle}>
                {[1, 2, 3, 4, 5].map(n => <option key={n} value={n}>{n} Star{n > 1 ? 's' : ''}</option>)}
              </select>
            </div>
            <textarea placeholder="Your review (optional)" value={reviewForm.comment}
              onChange={e => setReviewForm({ ...reviewForm, comment: e.target.value })}
              style={{ ...inputStyle, minHeight: '80px' }} />
            <div style={{ display: 'flex', gap: '10px' }}>
              <button type="submit" style={{
                padding: '10px 20px', backgroundColor: '#4361ee', color: '#fff',
                border: 'none', borderRadius: '6px', cursor: 'pointer'
              }}>
                Submit Review
              </button>
              <button type="button" onClick={() => setReviewForm({ bookingId: null, rating: 5, comment: '' })}
                style={{
                  padding: '10px 20px', border: '1px solid var(--border-color)',
                  borderRadius: '6px', cursor: 'pointer',
                  backgroundColor: 'var(--card-bg)', color: 'var(--text-primary)'
                }}>
                Cancel
              </button>
            </div>
          </form>
        </div>
      )}
    </div>
  );
}

export default Dashboard;
