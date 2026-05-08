package com.sensei.teacher;

import com.sensei.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "teacher_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "teacher_subjects", joinColumns = @JoinColumn(name = "teacher_id"))
    @Column(name = "subject")
    private Set<String> subjects; // e.g., "Mathematics", "Physics", "Guitar"

    @Column(nullable = false)
    private BigDecimal hourlyRate;

    @Column(nullable = false)
    @Builder.Default
    private Double avgRating = 0.0;

    @Column(nullable = false)
    @Builder.Default
    private Integer totalBookings = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer totalReviews = 0;

    private String profilePhotoKey; // S3 object key

    private String certificateKey; // S3 object key

    private Integer experienceYears;

    private String city;

    private LocalDateTime lastActiveAt;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        lastActiveAt = LocalDateTime.now();
    }

    // Profile completeness score (0.0 to 1.0)
    public double getProfileCompleteness() {
        double score = 0.0;
        if (bio != null && !bio.isEmpty()) score += 0.25;
        if (profilePhotoKey != null) score += 0.25;
        if (certificateKey != null) score += 0.25;
        if (subjects != null && !subjects.isEmpty()) score += 0.25;
        return score;
    }
}
