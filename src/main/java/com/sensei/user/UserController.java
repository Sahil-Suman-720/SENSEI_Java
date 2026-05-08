package com.sensei.user;

import com.sensei.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserDto.AuthResponse>> signup(@Valid @RequestBody UserDto.SignupRequest request) {
        UserDto.AuthResponse response = userService.signup(request);
        return ResponseEntity.ok(ApiResponse.success("Registration successful", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserDto.AuthResponse>> login(@Valid @RequestBody UserDto.LoginRequest request) {
        UserDto.AuthResponse response = userService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDto.UserProfileResponse>> getMyProfile(
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        UserDto.UserProfileResponse profile = userService.getProfile(user.getId());
        return ResponseEntity.ok(ApiResponse.success(profile));
    }
}
