package com.sensei.recommendation;

import com.sensei.teacher.TeacherDto;
import com.sensei.teacher.TeacherProfile;
import com.sensei.teacher.TeacherRepository;
import com.sensei.user.User;
import com.sensei.user.UserRepository;
import com.sensei.storage.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    /**
     * Content-Based Filtering + Weighted Scoring
     *
     * Step 1: Filter teachers whose subjects overlap with student's interests (content-based)
     * Step 2: Score each teacher using: 0.4*rating + 0.3*bookings_norm + 0.2*profile_completeness + 0.1*recency
     * Step 3: Return top N sorted by score
     */
    public List<TeacherDto.TeacherResponse> getRecommendations(Long studentId, int limit) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Set<String> interests = student.getInterests();
        if (interests == null || interests.isEmpty()) {
            // Fallback: return top-rated teachers if no interests set
            return teacherRepository.findAll().stream()
                    .sorted(Comparator.comparingDouble(TeacherProfile::getAvgRating).reversed())
                    .limit(limit)
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        }

        // Step 1: Content-based filter — find teachers with matching subjects
        List<TeacherProfile> matchedTeachers = teacherRepository.findBySubjectsIn(new ArrayList<>(interests));

        if (matchedTeachers.isEmpty()) {
            return Collections.emptyList();
        }

        // Find max bookings for normalization
        int maxBookings = matchedTeachers.stream()
                .mapToInt(TeacherProfile::getTotalBookings)
                .max()
                .orElse(1); // avoid division by zero

        // Step 2: Score each teacher
        List<ScoredTeacher> scoredTeachers = matchedTeachers.stream()
                .map(teacher -> {
                    double score = calculateScore(teacher, maxBookings);
                    return new ScoredTeacher(teacher, score);
                })
                .sorted(Comparator.comparingDouble(ScoredTeacher::score).reversed())
                .limit(limit)
                .toList();

        // Step 3: Return sorted results
        return scoredTeachers.stream()
                .map(st -> mapToResponse(st.teacher()))
                .collect(Collectors.toList());
    }

    /**
     * Weighted scoring formula:
     * score = 0.4 * (avgRating / 5.0)
     *       + 0.3 * (totalBookings / maxBookings)
     *       + 0.2 * profileCompleteness
     *       + 0.1 * recencyScore
     */
    private double calculateScore(TeacherProfile teacher, int maxBookings) {
        // Rating component (normalized to 0-1)
        double ratingScore = (teacher.getAvgRating() != null ? teacher.getAvgRating() : 0.0) / 5.0;

        // Bookings component (normalized to 0-1)
        double bookingsScore = maxBookings > 0
                ? (double) teacher.getTotalBookings() / maxBookings
                : 0.0;

        // Profile completeness (already 0-1)
        double completenessScore = teacher.getProfileCompleteness();

        // Recency score — more recent activity = higher score
        double recencyScore = calculateRecencyScore(teacher.getLastActiveAt());

        return (0.4 * ratingScore) + (0.3 * bookingsScore) + (0.2 * completenessScore) + (0.1 * recencyScore);
    }

    /**
     * Recency score: 1.0 if active today, decreasing over 30 days to 0.0
     */
    private double calculateRecencyScore(LocalDateTime lastActiveAt) {
        if (lastActiveAt == null) return 0.0;
        long daysSinceActive = ChronoUnit.DAYS.between(lastActiveAt, LocalDateTime.now());
        if (daysSinceActive <= 0) return 1.0;
        if (daysSinceActive >= 30) return 0.0;
        return 1.0 - (daysSinceActive / 30.0);
    }

    private TeacherDto.TeacherResponse mapToResponse(TeacherProfile profile) {
        TeacherDto.TeacherResponse response = new TeacherDto.TeacherResponse();
        response.setId(profile.getId());
        response.setName(profile.getUser().getName());
        response.setBio(profile.getBio());
        response.setSubjects(profile.getSubjects());
        response.setHourlyRate(profile.getHourlyRate());
        response.setAvgRating(profile.getAvgRating());
        response.setTotalBookings(profile.getTotalBookings());
        response.setTotalReviews(profile.getTotalReviews());
        response.setExperienceYears(profile.getExperienceYears());
        response.setCity(profile.getCity());
        if (profile.getProfilePhotoKey() != null) {
            response.setProfilePhotoUrl(fileStorageService.generateDownloadUrl(profile.getProfilePhotoKey()));
        }
        return response;
    }

    private record ScoredTeacher(TeacherProfile teacher, double score) {}
}
