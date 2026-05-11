import { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { bookingApi, reviewApi, teacherApi } from '../services/api';
import StarRating from '../components/StarRating';

function Dashboard() {
  const { user } = useAuth();
  const [bookings, setBookings] = useState([]);
  const [teacherProfile, setTeacherProfile] = useState(null);
  const [newSkill, setNewSkill] = useState('');
  const [reviewForm, setReviewForm] = useState({ bookingId: null, rating: 0, comment: '' });
  const [message, setMessage] = useState('');

  useEffect(() => {
    if (user?.role === 'STUDENT') {
      bookingApi.getMyBookings().then(res => setBookings(res.data.data || [])).catch(() => {});
    } else if (user?.role === 'TEACHER') {
      bookingApi.getTeacherBookings().then(res => setBookings(res.data.data || [])).catch(() => {});
      teacherApi.getMyProfile().then(res => setTeacherProfile(res.data.data)).catch(() => {});
    }
  }, [user]);

  const handleMarkCompleted = async (bookingId) => {
    try {
      await bookingApi.markCompleted(bookingId);
      setBookings(bookings.map(b => b.id === bookingId ? { ...b, status: 'COMPLETED' } : b));
      setMessage('Session marked as completed! You can now write a review.');
    } catch (err) {
      setMessage(err.response?.data?.message || 'Failed to mark as completed');
    }
  };

  const handleConfirmBooking = async (bookingId) => {
    try {
      await bookingApi.confirmBooking(bookingId);
      setBookings(bookings.map(b => b.id === bookingId ? { ...b, status: 'CONFIRMED' } : b));
      setMessage('Booking confirmed!');
    } catch (err) {
      setMessage(err.response?.data?.message || 'Failed to confirm');
    }
  };

  const handleRejectBooking = async (bookingId) => {
    try {
      await bookingApi.rejectBooking(bookingId);
      setBookings(bookings.map(b => b.id === bookingId ? { ...b, status: 'CANCELLED' } : b));
      setMessage('Booking rejected.');
    } catch (err) {
      setMessage(err.response?.data?.message || 'Failed to reject');
    }
  };

  const handleAddSkill = async (e) => {
    e.preventDefault();
    if (!newSkill.trim()) return;
    try {
      const res = await teacherApi.addSubject(newSkill.trim());
      setTeacherProfile(res.data.data);
      setNewSkill('');
      setMessage('Skill added!');
    } catch (err) {
      setMessage(err.response?.data?.message || 'Failed to add skill');
    }
  };

  const handleReview = async (e) => {
    e.preventDefault();
    if (reviewForm.rating === 0) {
      setMessage('Please select a rating');
      return;
    }
    try {
      await reviewApi.create(reviewForm);
      setMessage('Review submitted! Teacher rating updated.');
      setReviewForm({ bookingId: null, rating: 0, comment: '' });
      // Refresh bookings
      bookingApi.getMyBookings().then(res => setBookings(res.data.data || [])).catch(() => {});
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
      <p style={{ color: 'var(--text-muted)', marginBottom: '16px' }}>Welcome, {user.name} ({user.role})</p>
      {message && <p style={{ color: message.includes('submitted') || message.includes('completed') || message.includes('added') || message.includes('confirmed') ? '#27ae60' : '#e74c3c', marginBottom: '16px' }}>{message}</p>}

      {/* Teacher: Show skills + add skill + rating */}
      {user.role === 'TEACHER' && teacherProfile && (
        <div style={{ marginBottom: '24px', padding: '16px', backgroundColor: 'var(--card-bg)', borderRadius: '8px', border: '1px solid var(--border-color)' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '12px' }}>
            <h3 style={{ color: 'var(--text-primary)', fontSize: '1rem' }}>My Skills/Subjects</h3>
            <span style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>
              ⭐ {teacherProfile.avgRating?.toFixed(1) || '0.0'} ({teacherProfile.totalReviews || 0} reviews)
            </span>
          </div>
          <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px', marginBottom: '12px' }}>
            {teacherProfile.subjects?.map(sub => (
              <span key={sub} style={{
                padding: '6px 14px', backgroundColor: 'var(--tag-bg)',
                borderRadius: '20px', fontSize: '0.9rem', color: 'var(--text-secondary)'
              }}>
                {sub}
              </span>
            ))}
            {(!teacherProfile.subjects || teacherProfile.subjects.length === 0) && (
              <p style={{ color: 'var(--text-muted)' }}>No skills added yet.</p>
            )}
          </div>
          <form onSubmit={handleAddSkill} style={{ display: 'flex', gap: '8px' }}>
            <input value={newSkill} onChange={e => setNewSkill(e.target.value)}
              placeholder="Add a new skill (e.g., Mathematics)"
              style={{ flex: 1, padding: '8px 12px', borderRadius: '6px', border: '1px solid var(--input-border)', backgroundColor: 'var(--input-bg)', color: 'var(--text-primary)' }} />
            <button type="submit" style={{
              padding: '8px 16px', backgroundColor: '#4361ee', color: '#fff',
              border: 'none', borderRadius: '6px', cursor: 'pointer'
            }}>
              Add
            </button>
          </form>
        </div>
      )}

      {/* Student: Show interests */}
      {user.role === 'STUDENT' && user.interests && user.interests.length > 0 && (
        <div style={{ marginBottom: '24px', padding: '16px', backgroundColor: 'var(--card-bg)', borderRadius: '8px', border: '1px solid var(--border-color)' }}>
          <h3 style={{ color: 'var(--text-primary)', marginBottom: '10px', fontSize: '1rem' }}>My Interests</h3>
          <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px' }}>
            {user.interests.map(interest => (
              <span key={interest} style={{
                padding: '6px 14px', backgroundColor: 'var(--tag-bg)',
                borderRadius: '20px', fontSize: '0.9rem', color: 'var(--text-secondary)'
              }}>
                {interest}
              </span>
            ))}
          </div>
        </div>
      )}

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

              {/* Teacher: Confirm/Reject PENDING bookings */}
              {user.role === 'TEACHER' && booking.status === 'PENDING' && (
                <div style={{ marginTop: '8px', display: 'flex', gap: '8px', justifyContent: 'flex-end' }}>
                  <button onClick={() => handleConfirmBooking(booking.id)} style={{
                    padding: '4px 12px', backgroundColor: '#27ae60', color: '#fff',
                    border: 'none', borderRadius: '4px', cursor: 'pointer', fontSize: '0.85rem'
                  }}>
                    Confirm
                  </button>
                  <button onClick={() => handleRejectBooking(booking.id)} style={{
                    padding: '4px 12px', backgroundColor: '#e74c3c', color: '#fff',
                    border: 'none', borderRadius: '4px', cursor: 'pointer', fontSize: '0.85rem'
                  }}>
                    Reject
                  </button>
                </div>
              )}

              {/* Student: Mark Completed on CONFIRMED bookings */}
              {user.role === 'STUDENT' && booking.status === 'CONFIRMED' && (
                <button onClick={() => handleMarkCompleted(booking.id)}
                  style={{
                    display: 'block', marginTop: '8px', color: '#27ae60',
                    background: 'none', border: '1px solid #27ae60', borderRadius: '4px',
                    padding: '4px 10px', cursor: 'pointer', fontSize: '0.85rem'
                  }}>
                  Mark Completed
                </button>
              )}

              {/* Student: Write Review on COMPLETED bookings */}
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

      {/* Review Form with Star Rating */}
      {reviewForm.bookingId && (
        <div style={{
          marginTop: '30px', border: '1px solid var(--border-color)',
          borderRadius: '8px', padding: '20px', backgroundColor: 'var(--card-bg)'
        }}>
          <h3 style={{ color: 'var(--text-primary)', marginBottom: '16px' }}>Write a Review</h3>
          <form onSubmit={handleReview} style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
            <div>
              <label style={{ color: 'var(--text-secondary)', display: 'block', marginBottom: '8px' }}>Rate your experience:</label>
              <StarRating rating={reviewForm.rating} onRate={(r) => setReviewForm({ ...reviewForm, rating: r })} size={32} />
            </div>
            <textarea placeholder="Tell us about your experience (optional)" value={reviewForm.comment}
              onChange={e => setReviewForm({ ...reviewForm, comment: e.target.value })}
              style={{ ...inputStyle, minHeight: '80px' }} />
            <div style={{ display: 'flex', gap: '10px' }}>
              <button type="submit" style={{
                padding: '10px 20px', backgroundColor: '#4361ee', color: '#fff',
                border: 'none', borderRadius: '6px', cursor: 'pointer'
              }}>
                Submit Review
              </button>
              <button type="button" onClick={() => setReviewForm({ bookingId: null, rating: 0, comment: '' })}
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
