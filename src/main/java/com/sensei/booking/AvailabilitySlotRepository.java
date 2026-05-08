package com.sensei.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.DayOfWeek;
import java.util.List;

public interface AvailabilitySlotRepository extends JpaRepository<AvailabilitySlot, Long> {

    List<AvailabilitySlot> findByTeacherIdAndIsActiveTrue(Long teacherId);

    List<AvailabilitySlot> findByTeacherIdAndDayOfWeekAndIsActiveTrue(Long teacherId, DayOfWeek dayOfWeek);

    @Query("SELECT s FROM AvailabilitySlot s WHERE s.teacher.id = :teacherId AND s.isActive = true ORDER BY s.dayOfWeek, s.startTime")
    List<AvailabilitySlot> findAllActiveSlotsByTeacher(@Param("teacherId") Long teacherId);
}
