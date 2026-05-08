package com.sensei.teacher;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Set;

public class TeacherDto {

    @Data
    public static class CreateProfileRequest {
        @NotBlank(message = "Bio is required")
        private String bio;

        @NotNull(message = "Subjects are required")
        private Set<String> subjects;

        @NotNull(message = "Hourly rate is required")
        private BigDecimal hourlyRate;

        private Integer experienceYears;
        private String city;
    }

    @Data
    public static class UpdateProfileRequest {
        private String bio;
        private Set<String> subjects;
        private BigDecimal hourlyRate;
        private Integer experienceYears;
        private String city;
    }

    @Data
    public static class TeacherSearchRequest {
        private String query;        // text search
        private String subject;      // filter by subject
        private Double minRating;
        private BigDecimal minPrice;
        private BigDecimal maxPrice;
        private String city;
        private String sortBy;       // rating, price, bookings
        private String sortDir;      // asc, desc
    }

    @Data
    public static class TeacherResponse {
        private Long id;
        private String name;
        private String bio;
        private Set<String> subjects;
        private BigDecimal hourlyRate;
        private Double avgRating;
        private Integer totalBookings;
        private Integer totalReviews;
        private String profilePhotoUrl;
        private Integer experienceYears;
        private String city;
    }
}
