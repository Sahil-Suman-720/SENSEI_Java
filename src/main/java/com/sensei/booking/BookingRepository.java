package com.sensei.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByStudentIdOrderByCreatedAtDesc(Long studentId);

    List<Booking> findByTeacherIdOrderByCreatedAtDesc(Long teacherId);

    @Query("SELECT b FROM Booking b WHERE b.teacher.id = :teacherId AND b.bookingDate = :date AND b.status NOT IN ('CANCELLED', 'REFUNDED')")
    List<Booking> findActiveBookingsForTeacherOnDate(@Param("teacherId") Long teacherId, @Param("date") LocalDate date);

    // Check if slot is already booked for a specific date
    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.teacher.id = :teacherId AND b.slot.id = :slotId AND b.bookingDate = :date AND b.status NOT IN ('CANCELLED', 'REFUNDED')")
    boolean isSlotBooked(@Param("teacherId") Long teacherId, @Param("slotId") Long slotId, @Param("date") LocalDate date);

    Optional<Booking> findByRazorpayOrderId(String razorpayOrderId);
}
