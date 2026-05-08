package com.sensei.teacher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<TeacherProfile, Long>, JpaSpecificationExecutor<TeacherProfile> {

    Optional<TeacherProfile> findByUserId(Long userId);

    @Query("SELECT t FROM TeacherProfile t JOIN t.subjects s WHERE s IN :subjects ORDER BY t.avgRating DESC")
    List<TeacherProfile> findBySubjectsIn(@Param("subjects") List<String> subjects);

    @Query(value = """
        SELECT tp.* FROM teacher_profiles tp
        INNER JOIN teacher_subjects ts ON tp.id = ts.teacher_id
        WHERE ts.subject IN (:subjects)
        GROUP BY tp.id
        ORDER BY (0.4 * tp.avg_rating + 0.3 * (tp.total_bookings / (SELECT MAX(total_bookings) FROM teacher_profiles WHERE total_bookings > 0)) + 0.2 * 1.0 + 0.1 * DATEDIFF(NOW(), tp.last_active_at) * -0.01) DESC
        LIMIT :limit
    """, nativeQuery = true)
    List<TeacherProfile> findRecommendedTeachers(@Param("subjects") List<String> subjects, @Param("limit") int limit);
}
