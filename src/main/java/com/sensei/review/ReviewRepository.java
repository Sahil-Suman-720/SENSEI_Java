package com.sensei.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByTeacherIdOrderByCreatedAtDesc(Long teacherId);

    Optional<Review> findByBookingId(Long bookingId);

    boolean existsByBookingId(Long bookingId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.teacher.id = :teacherId")
    Double calculateAverageRating(@Param("teacherId") Long teacherId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.teacher.id = :teacherId")
    Long countByTeacherId(@Param("teacherId") Long teacherId);
}
