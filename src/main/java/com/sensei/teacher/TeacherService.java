package com.sensei.teacher;

import com.sensei.common.dto.ApiResponse;
import com.sensei.storage.FileStorageService;
import com.sensei.user.User;
import com.sensei.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    public TeacherDto.TeacherResponse createProfile(Long userId, TeacherDto.CreateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != User.Role.TEACHER) {
            throw new RuntimeException("Only users with TEACHER role can create a teacher profile");
        }

        if (teacherRepository.findByUserId(userId).isPresent()) {
            throw new RuntimeException("Teacher profile already exists");
        }

        TeacherProfile profile = TeacherProfile.builder()
                .user(user)
                .bio(request.getBio())
                .subjects(request.getSubjects())
                .hourlyRate(request.getHourlyRate())
                .experienceYears(request.getExperienceYears())
                .city(request.getCity())
                .build();

        profile = teacherRepository.save(profile);
        return mapToResponse(profile);
    }

    public TeacherDto.TeacherResponse updateProfile(Long userId, TeacherDto.UpdateProfileRequest request) {
        TeacherProfile profile = teacherRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Teacher profile not found"));

        if (request.getBio() != null) profile.setBio(request.getBio());
        if (request.getSubjects() != null) profile.setSubjects(request.getSubjects());
        if (request.getHourlyRate() != null) profile.setHourlyRate(request.getHourlyRate());
        if (request.getExperienceYears() != null) profile.setExperienceYears(request.getExperienceYears());
        if (request.getCity() != null) profile.setCity(request.getCity());

        profile = teacherRepository.save(profile);
        return mapToResponse(profile);
    }

    public TeacherDto.TeacherResponse getTeacherById(Long teacherId) {
        TeacherProfile profile = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        return mapToResponse(profile);
    }

    public TeacherDto.TeacherResponse getTeacherByUserId(Long userId) {
        TeacherProfile profile = teacherRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Teacher profile not found"));
        return mapToResponse(profile);
    }

    public TeacherDto.TeacherResponse addSubject(Long userId, String subject) {
        TeacherProfile profile = teacherRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Teacher profile not found"));
        if (subject == null || subject.trim().isEmpty()) {
            throw new RuntimeException("Subject cannot be empty");
        }
        profile.getSubjects().add(subject.trim());
        profile = teacherRepository.save(profile);
        return mapToResponse(profile);
    }

    public List<TeacherDto.TeacherResponse> searchTeachers(TeacherDto.TeacherSearchRequest request, int page, int size) {
        Specification<TeacherProfile> spec = Specification.where(null);

        // Name/keyword search — matches teacher name or bio
        if (request.getQuery() != null && !request.getQuery().isEmpty()) {
            String keyword = "%" + request.getQuery().toLowerCase() + "%";
            spec = spec.and((root, query, cb) ->
                    cb.or(
                            cb.like(cb.lower(root.get("user").get("name")), keyword),
                            cb.like(cb.lower(root.get("bio")), keyword)
                    ));
        }

        if (request.getSubject() != null && !request.getSubject().isEmpty()) {
            spec = spec.and((root, query, cb) -> {
                var join = root.<TeacherProfile, String>join("subjects");
                return cb.equal(cb.lower(join), request.getSubject().toLowerCase());
            });
        }

        if (request.getMinRating() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("avgRating"), request.getMinRating()));
        }

        if (request.getMinPrice() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("hourlyRate"), request.getMinPrice()));
        }

        if (request.getMaxPrice() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("hourlyRate"), request.getMaxPrice()));
        }

        if (request.getCity() != null && !request.getCity().isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(cb.lower(root.get("city")), request.getCity().toLowerCase()));
        }

        // Sorting
        String sortField = "avgRating";
        if ("price".equals(request.getSortBy())) sortField = "hourlyRate";
        else if ("bookings".equals(request.getSortBy())) sortField = "totalBookings";

        Sort sort = "asc".equalsIgnoreCase(request.getSortDir())
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return teacherRepository.findAll(spec, pageable)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
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
}
