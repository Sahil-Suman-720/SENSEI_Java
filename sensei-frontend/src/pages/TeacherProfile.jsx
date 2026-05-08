import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { teacherApi, bookingApi, reviewApi, paymentApi } from '../services/api';
import { useAuth } from '../context/AuthContext';

function TeacherProfile() {
  const { id } = useParams();
  const { user } = useAuth();
  const [teacher, setTeacher] = useState(null);
  const [reviews, setReviews] = useState([]);
  const [slots, setSlots] = useState([]);
  const [selectedDate, setSelectedDate] = useState('');
  const [message, setMessage] = useState('');
  const [slotsLoading, setSlotsLoading] = useState(false);
  const [slotsChecked, setSlotsChecked] = useState(false);

  useEffect(() => {
    teacherApi.getById(id).then(res => setTeacher(res.data.data));
    reviewApi.getTeacherReviews(id).then(res => setReviews(res.data.data || [])).catch(() => {});
  }, [id]);

  const loadSlots = async () => {
    if (!selectedDate) {
      setMessage('Please select a date first');
      return;
    }
    setMessage('');
    setSlotsLoading(true);
    setSlotsChecked(false);
    try {
      const res = await bookingApi.getSlots(id, selectedDate);
      setSlots(res.data.data || []);
      setSlotsChecked(true);
    } catch (err) {
      setMessage(err.response?.data?.message || 'Failed to load slots');
    }
    setSlotsLoading(false);
  };

  const handleBook = async (slotId) => {
    try {
      const bookRes = await bookingApi.createBooking({
        teacherId: parseInt(id),
        slotId,
        bookingDate: selectedDate
      });
      const booking = bookRes.data.data;

      // If Razorpay is configured, initiate payment
      try {
        const payRes = await paymentApi.initiate(booking.id);
        const order = payRes.data.data;

        const options = {
          key: order.keyId,
          amount: order.amount,
          currency: order.currency,
          name: 'SENSEI',
          description: `Session with ${teacher.name}`,
          order_id: order.razorpayOrderId,
          handler: async function (response) {
            await paymentApi.verify({
              razorpayOrderId: response.razorpay_order_id,
              razorpayPaymentId: response.razorpay_payment_id,
              razorpaySignature: response.razorpay_signature,
            });
            setMessage('Booking confirmed! Payment successful.');
            loadSlots();
          },
          prefill: { email: user?.email },
        };

        const rzp = new window.Razorpay(options);
        rzp.open();
      } catch (payErr) {
        // Razorpay not configured — booking created but payment pending
        setMessage('Booking created (Payment gateway not configured yet). Status: ' + booking.status);
        loadSlots();
      }
    } catch (err) {
      setMessage(err.response?.data?.message || 'Booking failed');
    }
  };

  if (!teacher) return <p style={{ color: 'var(--text-muted)', padding: '40px', textAlign: 'center' }}>Loading...</p>;

  return (
    <div>
      <div style={{ display: 'flex', gap: '30px', marginBottom: '40px', flexWrap: 'wrap' }}>
        <div style={{ flex: '0 0 200px' }}>
          {teacher.profilePhotoUrl ? (
            <img src={teacher.profilePhotoUrl} alt={teacher.name}
              style={{ width: '200px', height: '200px', borderRadius: '12px', objectFit: 'cover' }} />
          ) : (
            <div style={{
              width: '200px', height: '200px', borderRadius: '12px',
              backgroundColor: 'var(--toggle-bg)', display: 'flex', alignItems: 'center',
              justifyContent: 'center', color: 'var(--text-muted)', fontSize: '0.9rem'
            }}>
              No Photo
            </div>
          )}
        </div>

        <div style={{ flex: 1 }}>
          <h1 style={{ color: 'var(--text-primary)' }}>{teacher.name}</h1>
          {teacher.city && <p style={{ color: 'var(--text-muted)' }}>{teacher.city}</p>}
          <p style={{ color: 'var(--text-secondary)', marginTop: '8px' }}>{teacher.bio}</p>

          <div style={{ display: 'flex', gap: '20px', marginTop: '16px', color: 'var(--text-secondary)' }}>
            <span>⭐ {teacher.avgRating?.toFixed(1) || 'New'} ({teacher.totalReviews} reviews)</span>
            <span>{teacher.totalBookings} sessions</span>
            <span>{teacher.experienceYears || '—'} years exp</span>
          </div>

          <div style={{ marginTop: '12px', display: 'flex', gap: '6px', flexWrap: 'wrap' }}>
            {teacher.subjects?.map(sub => (
              <span key={sub} style={{
                padding: '4px 12px', backgroundColor: 'var(--tag-bg)',
                borderRadius: '20px', color: 'var(--text-secondary)', fontSize: '0.85rem'
              }}>
                {sub}
              </span>
            ))}
          </div>

          <p style={{ fontSize: '1.5rem', fontWeight: 'bold', color: 'var(--accent)', marginTop: '16px' }}>
            ₹{teacher.hourlyRate}/hr
          </p>
        </div>
      </div>

      {/* Booking Section */}
      {user && user.role === 'STUDENT' && (
        <section style={{
          border: '1px solid var(--border-color)', borderRadius: '12px',
          padding: '20px', marginBottom: '30px', backgroundColor: 'var(--card-bg)'
        }}>
          <h2 style={{ color: 'var(--text-primary)', marginBottom: '16px' }}>Book a Session</h2>
          {message && <p style={{ color: message.includes('confirmed') || message.includes('created') ? '#27ae60' : '#e74c3c', marginBottom: '12px' }}>{message}</p>}

          <div style={{ display: 'flex', gap: '12px', alignItems: 'center', marginBottom: '16px', flexWrap: 'wrap' }}>
            <input type="date" value={selectedDate} onChange={e => { setSelectedDate(e.target.value); setSlotsChecked(false); setSlots([]); }}
              min={new Date().toISOString().split('T')[0]}
              style={{ padding: '10px', borderRadius: '6px', border: '1px solid var(--input-border)', backgroundColor: 'var(--input-bg)', color: 'var(--text-primary)' }} />
            <button onClick={loadSlots} disabled={slotsLoading} style={{
              padding: '10px 20px', backgroundColor: '#4361ee', color: '#fff',
              border: 'none', borderRadius: '6px', cursor: slotsLoading ? 'wait' : 'pointer',
              opacity: slotsLoading ? 0.7 : 1
            }}>
              {slotsLoading ? 'Checking...' : 'Check Availability'}
            </button>
          </div>

          {slotsChecked && slots.length > 0 && (
            <div style={{ display: 'flex', gap: '10px', flexWrap: 'wrap' }}>
              {slots.map(slot => (
                <button key={slot.id} disabled={slot.booked}
                  onClick={() => handleBook(slot.id)}
                  style={{
                    padding: '10px 16px', borderRadius: '6px',
                    cursor: slot.booked ? 'not-allowed' : 'pointer',
                    border: '1px solid var(--border-color)',
                    backgroundColor: slot.booked ? 'var(--toggle-bg)' : 'var(--card-bg)',
                    color: slot.booked ? 'var(--text-muted)' : 'var(--text-primary)',
                    opacity: slot.booked ? 0.6 : 1
                  }}>
                  {slot.startTime} - {slot.endTime} {slot.booked ? '(Booked)' : ''}
                </button>
              ))}
            </div>
          )}

          {slotsChecked && slots.length === 0 && (
            <p style={{ color: 'var(--text-muted)', fontStyle: 'italic' }}>
              No available slots for this date. Try another date or contact the teacher.
            </p>
          )}
        </section>
      )}

      {/* Reviews Section */}
      <section>
        <h2 style={{ color: 'var(--text-primary)', marginBottom: '16px' }}>Reviews ({reviews.length})</h2>
        {reviews.length === 0 && <p style={{ color: 'var(--text-muted)' }}>No reviews yet.</p>}
        {reviews.map(review => (
          <div key={review.id} style={{ borderBottom: '1px solid var(--border-color)', padding: '16px 0' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between' }}>
              <strong style={{ color: 'var(--text-primary)' }}>{review.studentName}</strong>
              <span>{'⭐'.repeat(review.rating)}</span>
            </div>
            {review.comment && <p style={{ margin: '8px 0 0', color: 'var(--text-secondary)' }}>{review.comment}</p>}
          </div>
        ))}
      </section>
    </div>
  );
}

export default TeacherProfile;
