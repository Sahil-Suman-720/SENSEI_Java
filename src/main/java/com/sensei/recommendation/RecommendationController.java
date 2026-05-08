package com.sensei.recommendation;

import com.sensei.common.dto.ApiResponse;
import com.sensei.teacher.TeacherDto;
import com.sensei.user.User;
import com.sensei.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<List<TeacherDto.TeacherResponse>>> getRecommendations(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "10") int limit) {
        User user = userService.findByEmail(userDetails.getUsername());
        List<TeacherDto.TeacherResponse> recommendations = recommendationService.getRecommendations(user.getId(), limit);
        return ResponseEntity.ok(ApiResponse.success(recommendations));
    }
}
