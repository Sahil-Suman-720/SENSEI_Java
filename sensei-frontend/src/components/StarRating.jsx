import { useState } from 'react';

function StarRating({ rating, onRate, size = 28 }) {
  const [hovered, setHovered] = useState(0);

  return (
    <div style={{ display: 'inline-flex', gap: '4px', cursor: 'pointer' }}>
      {[1, 2, 3, 4, 5].map(star => (
        <span
          key={star}
          onMouseEnter={() => setHovered(star)}
          onMouseLeave={() => setHovered(0)}
          onClick={() => onRate(star)}
          style={{
            fontSize: `${size}px`,
            color: star <= (hovered || rating) ? '#f5a623' : '#ddd',
            transition: 'color 0.15s, transform 0.15s',
            transform: star <= hovered ? 'scale(1.2)' : 'scale(1)',
            userSelect: 'none'
          }}
        >
          ★
        </span>
      ))}
      {rating > 0 && (
        <span style={{ marginLeft: '8px', fontSize: '0.9rem', color: 'var(--text-muted)', alignSelf: 'center' }}>
          {rating}/5
        </span>
      )}
    </div>
  );
}

export default StarRating;
