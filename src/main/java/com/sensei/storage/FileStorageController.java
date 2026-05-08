package com.sensei.storage;

import com.sensei.common.dto.ApiResponse;
import com.sensei.teacher.TeacherProfile;
import com.sensei.teacher.TeacherRepository;
import com.sensei.user.User;
import com.sensei.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileStorageController {

    private final FileStorageService fileStorageService;
    private final TeacherRepository teacherRepository;
    private final UserService userService;

    /**
     * Teacher requests a pre-signed upload URL.
     * Frontend then uploads the file directly to S3 using this URL.
     */
    @PostMapping("/upload-url")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<FileStorageDto.UploadUrlResponse>> getUploadUrl(
            @RequestBody FileStorageDto.UploadUrlRequest request) {
        FileStorageDto.UploadUrlResponse response = fileStorageService.generateUploadUrl(
                request.getFolder(), request.getFileName());
        return ResponseEntity.ok(ApiResponse.success("Upload URL generated", response));
    }

    /**
     * After uploading to S3, frontend sends the object key to save in teacher profile.
     */
    @PostMapping("/save-key")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<String>> saveFileKey(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody FileStorageDto.SaveFileKeyRequest request) {
        User user = userService.findByEmail(userDetails.getUsername());
        TeacherProfile profile = teacherRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Teacher profile not found"));

        if ("photo".equals(request.getType())) {
            profile.setProfilePhotoKey(request.getObjectKey());
        } else if ("certificate".equals(request.getType())) {
            profile.setCertificateKey(request.getObjectKey());
        } else {
            throw new RuntimeException("Invalid file type. Use 'photo' or 'certificate'");
        }

        teacherRepository.save(profile);
        return ResponseEntity.ok(ApiResponse.success("File key saved", request.getObjectKey()));
    }
}
