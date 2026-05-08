import { useState } from 'react';
import { Link } from 'react-router-dom';
import { teacherApi } from '../services/api';

function SearchTeachers() {
  const [query, setQuery] = useState('');
  const [subject, setSubject] = useState('');
  const [maxPrice, setMaxPrice] = useState('');
  const [teachers, setTeachers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(false);

  const handleSearch = async (e, pageNum = 0) => {
    if (e) e.preventDefault();
    setLoading(true);
    try {
      const params = { page: pageNum, size: 10 };
      if (query) params.query = query;
      if (subject) params.subject = subject;
      if (maxPrice) params.maxPrice = maxPrice;
      params.sortBy = 'rating';
      params.sortDir = 'desc';

      const res = await teacherApi.search(params);
      const results = res.data.data || [];
      setTeachers(results);
      setPage(pageNum);
      setHasMore(results.length === 10);
    } catch (err) {
      console.error('Search failed:', err);
    }
    setLoading(false);
  };

  const inputStyle = {
    padding: '12px', borderRadius: '6px',
    border: '1px solid var(--input-border)',
    backgroundColor: 'var(--input-bg)',
    color: 'var(--text-primary)'
  };

  return (
    <div>
      <h2 style={{ color: 'var(--text-primary)', marginBottom: '20px' }}>Find Teachers</h2>

      <form onSubmit={handleSearch} style={{ display: 'flex', gap: '12px', marginBottom: '30px', flexWrap: 'wrap' }}>
        <input placeholder="Search by name or keyword..." value={query}
          onChange={e => setQuery(e.target.value)}
          style={{ ...inputStyle, flex: '1', minWidth: '200px' }} />

        <input placeholder="Subject (e.g., Mathematics)" value={subject}
          onChange={e => setSubject(e.target.value)}
          style={{ ...inputStyle, width: '200px' }} />

        <input type="number" placeholder="Max Price/hr" value={maxPrice}
          onChange={e => setMaxPrice(e.target.value)}
          style={{ ...inputStyle, width: '150px' }} />

        <button type="submit" style={{
          padding: '12px 24px', backgroundColor: '#4361ee', color: '#fff',
          border: 'none', borderRadius: '6px', cursor: 'pointer'
        }}>
          Search
        </button>
      </form>

      {loading && <p style={{ color: 'var(--text-muted)' }}>Searching...</p>}

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: '20px' }}>
        {teachers.map(teacher => (
          <Link to={`/teachers/${teacher.id}`} key={teacher.id} style={{ textDecoration: 'none', color: 'inherit' }}>
            <div style={{
              border: '1px solid var(--border-color)', borderRadius: '12px',
              padding: '20px', backgroundColor: 'var(--card-bg)', transition: 'transform 0.2s'
            }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
                <div>
                  <h3 style={{ margin: 0, color: 'var(--text-primary)' }}>{teacher.name}</h3>
                  {teacher.city && <p style={{ margin: 0, color: 'var(--text-muted)', fontSize: '0.9rem' }}>{teacher.city}</p>}
                </div>
              </div>

              <p style={{ color: 'var(--text-secondary)', margin: '12px 0' }}>{teacher.bio?.substring(0, 100)}...</p>

              <div style={{ display: 'flex', flexWrap: 'wrap', gap: '6px', marginBottom: '12px' }}>
                {teacher.subjects?.map(sub => (
                  <span key={sub} style={{
                    padding: '4px 10px', backgroundColor: 'var(--tag-bg)',
                    borderRadius: '20px', fontSize: '0.85rem', color: 'var(--text-secondary)'
                  }}>
                    {sub}
                  </span>
                ))}
              </div>

              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <span style={{ color: 'var(--text-secondary)' }}>⭐ {teacher.avgRating?.toFixed(1) || 'New'} ({teacher.totalReviews} reviews)</span>
                <span style={{ fontWeight: 'bold', color: 'var(--accent)' }}>₹{teacher.hourlyRate}/hr</span>
              </div>
            </div>
          </Link>
        ))}
      </div>

      {!loading && teachers.length === 0 && (
        <p style={{ textAlign: 'center', color: 'var(--text-muted)', marginTop: '40px' }}>
          No teachers found. Try adjusting your search filters.
        </p>
      )}

      {/* Pagination */}
      {teachers.length > 0 && (
        <div style={{ display: 'flex', justifyContent: 'center', gap: '12px', marginTop: '30px' }}>
          {page > 0 && (
            <button onClick={() => handleSearch(null, page - 1)} style={{
              padding: '10px 20px', border: '1px solid var(--border-color)',
              borderRadius: '6px', backgroundColor: 'var(--card-bg)',
              color: 'var(--text-primary)', cursor: 'pointer'
            }}>
              Previous
            </button>
          )}
          <span style={{ padding: '10px', color: 'var(--text-muted)' }}>Page {page + 1}</span>
          {hasMore && (
            <button onClick={() => handleSearch(null, page + 1)} style={{
              padding: '10px 20px', border: '1px solid var(--border-color)',
              borderRadius: '6px', backgroundColor: 'var(--card-bg)',
              color: 'var(--text-primary)', cursor: 'pointer'
            }}>
              Next
            </button>
          )}
        </div>
      )}
    </div>
  );
}

export default SearchTeachers;
