package com.sensei.review;

import com.sensei.common.dto.ApiResponse;
import com.sensei.user.User;
import com.sensei.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<ReviewDto.ReviewResponse>> createReview(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ReviewDto.CreateReviewRequest request) {
        User user = userService.findByEmail(userDetails.getUsername());
        ReviewDto.ReviewResponse response = reviewService.createReview(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Review submitted", response));
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<ApiResponse<List<ReviewDto.ReviewResponse>>> getTeacherReviews(
            @PathVariable Long teacherId) {
        List<ReviewDto.ReviewResponse> reviews = reviewService.getTeacherReviews(teacherId);
        return ResponseEntity.ok(ApiResponse.success(reviews));
    }
}
