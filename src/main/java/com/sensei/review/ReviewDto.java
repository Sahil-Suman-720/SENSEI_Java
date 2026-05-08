package com.sensei.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

public class ReviewDto {

    @Data
    public static class CreateReviewRequest {
        @NotNull(message = "Booking ID is required")
        private Long bookingId;

        @NotNull(message = "Rating is required")
        @Min(value = 1, message = "Rating must be between 1 and 5")
        @Max(value = 5, message = "Rating must be between 1 and 5")
        private Integer rating;

        private String comment;
    }

    @Data
    public static class ReviewResponse {
        private Long id;
        private String studentName;
        private Integer rating;
        private String comment;
        private LocalDateTime createdAt;
    }
}
