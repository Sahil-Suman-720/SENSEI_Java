package com.sensei.teacher;

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
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;
    private final UserService userService;

    @PostMapping("/profile")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<TeacherDto.TeacherResponse>> createProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody TeacherDto.CreateProfileRequest request) {
        User user = userService.findByEmail(userDetails.getUsername());
        TeacherDto.TeacherResponse response = teacherService.createProfile(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Profile created", response));
    }

    @PutMapping("/profile")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<TeacherDto.TeacherResponse>> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody TeacherDto.UpdateProfileRequest request) {
        User user = userService.findByEmail(userDetails.getUsername());
        TeacherDto.TeacherResponse response = teacherService.updateProfile(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Profile updated", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TeacherDto.TeacherResponse>> getTeacher(@PathVariable Long id) {
        TeacherDto.TeacherResponse response = teacherService.getTeacherById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<TeacherDto.TeacherResponse>>> searchTeachers(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) java.math.BigDecimal minPrice,
            @RequestParam(required = false) java.math.BigDecimal maxPrice,
            @RequestParam(required = false) String city,
            @RequestParam(required = false, defaultValue = "rating") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortDir,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {

        TeacherDto.TeacherSearchRequest request = new TeacherDto.TeacherSearchRequest();
        request.setQuery(query);
        request.setSubject(subject);
        request.setMinRating(minRating);
        request.setMinPrice(minPrice);
        request.setMaxPrice(maxPrice);
        request.setCity(city);
        request.setSortBy(sortBy);
        request.setSortDir(sortDir);

        List<TeacherDto.TeacherResponse> results = teacherService.searchTeachers(request, page, size);
        return ResponseEntity.ok(ApiResponse.success(results));
    }
}
