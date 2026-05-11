import axios from 'axios';

const API_BASE = '/api';

const api = axios.create({
  baseURL: API_BASE,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Attach JWT token to every request
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Handle 401 — redirect to login
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// --- Auth ---
export const authApi = {
  signup: (data) => api.post('/auth/signup', data),
  login: (data) => api.post('/auth/login', data),
  getMe: () => api.get('/auth/me'),
};

// --- Teachers ---
export const teacherApi = {
  search: (params) => api.get('/teachers/search', { params }),
  getById: (id) => api.get(`/teachers/${id}`),
  getMyProfile: () => api.get('/teachers/me'),
  createProfile: (data) => api.post('/teachers/profile', data),
  updateProfile: (data) => api.put('/teachers/profile', data),
  addSubject: (subject) => api.post('/teachers/subjects', { subject }),
};

// --- Bookings ---
export const bookingApi = {
  getSlots: (teacherId, date) => api.get(`/bookings/slots/${teacherId}`, { params: { date } }),
  createBooking: (data) => api.post('/bookings', data),
  getMyBookings: () => api.get('/bookings/my-bookings'),
  getTeacherBookings: () => api.get('/bookings/teacher-bookings'),
  createSlot: (data) => api.post('/bookings/slots', data),
  markCompleted: (bookingId) => api.put(`/bookings/${bookingId}/complete`),
  confirmBooking: (bookingId) => api.put(`/bookings/${bookingId}/confirm`),
  rejectBooking: (bookingId) => api.put(`/bookings/${bookingId}/reject`),
};

// --- Payments ---
export const paymentApi = {
  initiate: (bookingId) => api.post('/payments/initiate', { bookingId }),
  verify: (data) => api.post('/payments/verify', data),
};

// --- Reviews ---
export const reviewApi = {
  create: (data) => api.post('/reviews', data),
  getTeacherReviews: (teacherId) => api.get(`/reviews/teacher/${teacherId}`),
};

// --- Recommendations ---
export const recommendationApi = {
  get: (limit = 10) => api.get('/recommendations', { params: { limit } }),
};

// --- File Storage ---
export const fileApi = {
  getUploadUrl: (folder, fileName) => api.post('/files/upload-url', { folder, fileName }),
  saveKey: (objectKey, type) => api.post('/files/save-key', { objectKey, type }),
};

export default api;
