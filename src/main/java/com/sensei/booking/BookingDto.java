package com.sensei.booking;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class BookingDto {

    @Data
    public static class CreateSlotRequest {
        @NotNull(message = "Day of week is required")
        private DayOfWeek dayOfWeek;

        @NotNull(message = "Start time is required")
        private LocalTime startTime;

        @NotNull(message = "End time is required")
        private LocalTime endTime;
    }

    @Data
    public static class CreateBookingRequest {
        @NotNull(message = "Teacher ID is required")
        private Long teacherId;

        @NotNull(message = "Slot ID is required")
        private Long slotId;

        @NotNull(message = "Booking date is required")
        private LocalDate bookingDate;
    }

    @Data
    public static class BookingResponse {
        private Long id;
        private String teacherName;
        private String studentName;
        private String subject;
        private LocalDate bookingDate;
        private LocalTime startTime;
        private LocalTime endTime;
        private String status;
        private String razorpayOrderId;
        private Double amount;
    }

    @Data
    public static class SlotResponse {
        private Long id;
        private DayOfWeek dayOfWeek;
        private LocalTime startTime;
        private LocalTime endTime;
        private boolean isBooked; // for a specific date
    }

    @Data
    public static class AvailableSlotsResponse {
        private Long teacherId;
        private String teacherName;
        private LocalDate date;
        private List<SlotResponse> slots;
    }
}
