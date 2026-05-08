package com.sensei.review;

import com.sensei.booking.Booking;
import com.sensei.booking.BookingRepository;
import com.sensei.teacher.TeacherProfile;
import com.sensei.teacher.TeacherRepository;
import com.sensei.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final TeacherRepository teacherRepository;

    @Transactional
    public ReviewDto.ReviewResponse createReview(Long studentId, ReviewDto.CreateReviewRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Validate student owns this booking
        if (!booking.getStudent().getId().equals(studentId)) {
            throw new RuntimeException("You can only review your own bookings");
        }

        // Validate booking is completed
        if (booking.getStatus() != Booking.BookingStatus.COMPLETED) {
            throw new RuntimeException("Can only review completed bookings");
        }

        // Check if already reviewed
        if (reviewRepository.existsByBookingId(request.getBookingId())) {
            throw new RuntimeException("You have already reviewed this booking");
        }

        Review review = Review.builder()
                .booking(booking)
                .student(booking.getStudent())
                .teacher(booking.getTeacher())
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        review = reviewRepository.save(review);

        // Recalculate teacher's average rating
        TeacherProfile teacher = booking.getTeacher();
        Double newAvgRating = reviewRepository.calculateAverageRating(teacher.getId());
        Long reviewCount = reviewRepository.countByTeacherId(teacher.getId());
        teacher.setAvgRating(newAvgRating != null ? newAvgRating : 0.0);
        teacher.setTotalReviews(reviewCount.intValue());
        teacherRepository.save(teacher);

        return mapToResponse(review);
    }

    public List<ReviewDto.ReviewResponse> getTeacherReviews(Long teacherId) {
        return reviewRepository.findByTeacherIdOrderByCreatedAtDesc(teacherId)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private ReviewDto.ReviewResponse mapToResponse(Review review) {
        ReviewDto.ReviewResponse response = new ReviewDto.ReviewResponse();
        response.setId(review.getId());
        response.setStudentName(review.getStudent().getName());
        response.setRating(review.getRating());
        response.setComment(review.getComment());
        response.setCreatedAt(review.getCreatedAt());
        return response;
    }
}
