package com.sensei.booking;

import com.sensei.common.dto.ApiResponse;
import com.sensei.user.User;
import com.sensei.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;

    // Teacher: create availability slot
    @PostMapping("/slots")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<BookingDto.SlotResponse>> createSlot(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody BookingDto.CreateSlotRequest request) {
        User user = userService.findByEmail(userDetails.getUsername());
        BookingDto.SlotResponse response = bookingService.createSlot(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Slot created", response));
    }

    // Anyone: view teacher's available slots for a date
    @GetMapping("/slots/{teacherId}")
    public ResponseEntity<ApiResponse<List<BookingDto.SlotResponse>>> getAvailableSlots(
            @PathVariable Long teacherId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<BookingDto.SlotResponse> slots = bookingService.getTeacherSlots(teacherId, date);
        return ResponseEntity.ok(ApiResponse.success(slots));
    }

    // Student: create a booking
    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<BookingDto.BookingResponse>> createBooking(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody BookingDto.CreateBookingRequest request) {
        User user = userService.findByEmail(userDetails.getUsername());
        BookingDto.BookingResponse response = bookingService.createBooking(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Booking created", response));
    }

    // Student: view my bookings
    @GetMapping("/my-bookings")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<List<BookingDto.BookingResponse>>> getMyBookings(
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        List<BookingDto.BookingResponse> bookings = bookingService.getStudentBookings(user.getId());
        return ResponseEntity.ok(ApiResponse.success(bookings));
    }

    // Teacher: view bookings for me
    @GetMapping("/teacher-bookings")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<List<BookingDto.BookingResponse>>> getTeacherBookings(
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        List<BookingDto.BookingResponse> bookings = bookingService.getTeacherBookings(user.getId());
        return ResponseEntity.ok(ApiResponse.success(bookings));
    }

    // Student: mark booking as completed
    @PutMapping("/{bookingId}/complete")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<BookingDto.BookingResponse>> markCompleted(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long bookingId) {
        User user = userService.findByEmail(userDetails.getUsername());
        BookingDto.BookingResponse response = bookingService.markAsCompleted(user.getId(), bookingId);
        return ResponseEntity.ok(ApiResponse.success("Session marked as completed", response));
    }

    // Teacher: confirm a pending booking
    @PutMapping("/{bookingId}/confirm")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<BookingDto.BookingResponse>> confirmBooking(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long bookingId) {
        User user = userService.findByEmail(userDetails.getUsername());
        BookingDto.BookingResponse response = bookingService.confirmBooking(user.getId(), bookingId);
        return ResponseEntity.ok(ApiResponse.success("Booking confirmed", response));
    }

    // Teacher: reject a pending booking
    @PutMapping("/{bookingId}/reject")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<BookingDto.BookingResponse>> rejectBooking(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long bookingId) {
        User user = userService.findByEmail(userDetails.getUsername());
        BookingDto.BookingResponse response = bookingService.rejectBooking(user.getId(), bookingId);
        return ResponseEntity.ok(ApiResponse.success("Booking rejected", response));
    }
}
