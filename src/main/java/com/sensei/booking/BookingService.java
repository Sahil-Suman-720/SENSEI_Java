package com.sensei.booking;

import com.sensei.teacher.TeacherProfile;
import com.sensei.teacher.TeacherRepository;
import com.sensei.user.User;
import com.sensei.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final AvailabilitySlotRepository slotRepository;
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;

    // --- Slot Management (Teacher) ---

    public BookingDto.SlotResponse createSlot(Long userId, BookingDto.CreateSlotRequest request) {
        TeacherProfile teacher = teacherRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Teacher profile not found"));

        if (request.getEndTime().isBefore(request.getStartTime()) || request.getEndTime().equals(request.getStartTime())) {
            throw new RuntimeException("End time must be after start time");
        }

        AvailabilitySlot slot = AvailabilitySlot.builder()
                .teacher(teacher)
                .dayOfWeek(request.getDayOfWeek())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .isActive(true)
                .build();

        slot = slotRepository.save(slot);
        return mapSlotToResponse(slot, false);
    }

    public List<BookingDto.SlotResponse> getTeacherSlots(Long teacherId, LocalDate date) {
        java.time.DayOfWeek dayOfWeek = date.getDayOfWeek();
        List<AvailabilitySlot> slots = slotRepository.findByTeacherIdAndDayOfWeekAndIsActiveTrue(teacherId, dayOfWeek);

        return slots.stream().map(slot -> {
            boolean booked = bookingRepository.isSlotBooked(teacherId, slot.getId(), date);
            return mapSlotToResponse(slot, booked);
        }).collect(Collectors.toList());
    }

    // --- Booking (Student) ---

    @Transactional
    public BookingDto.BookingResponse createBooking(Long studentId, BookingDto.CreateBookingRequest request) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        TeacherProfile teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        AvailabilitySlot slot = slotRepository.findById(request.getSlotId())
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        // Validate slot belongs to teacher
        if (!slot.getTeacher().getId().equals(teacher.getId())) {
            throw new RuntimeException("Slot does not belong to this teacher");
        }

        // Validate date is in the future
        if (request.getBookingDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Cannot book a slot in the past");
        }

        // Validate day of week matches
        if (request.getBookingDate().getDayOfWeek() != slot.getDayOfWeek()) {
            throw new RuntimeException("Booking date does not match the slot's day of week");
        }

        // Check if slot is already booked (CRITICAL: inside transaction)
        if (bookingRepository.isSlotBooked(teacher.getId(), slot.getId(), request.getBookingDate())) {
            throw new RuntimeException("This slot is already booked for the selected date");
        }

        Booking booking = Booking.builder()
                .student(student)
                .teacher(teacher)
                .slot(slot)
                .bookingDate(request.getBookingDate())
                .status(Booking.BookingStatus.PENDING)
                .amount(teacher.getHourlyRate())
                .build();

        booking = bookingRepository.save(booking);
        return mapBookingToResponse(booking);
    }

    public List<BookingDto.BookingResponse> getStudentBookings(Long studentId) {
        return bookingRepository.findByStudentIdOrderByCreatedAtDesc(studentId)
                .stream().map(this::mapBookingToResponse).collect(Collectors.toList());
    }

    public List<BookingDto.BookingResponse> getTeacherBookings(Long teacherId) {
        TeacherProfile teacher = teacherRepository.findByUserId(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher profile not found"));
        return bookingRepository.findByTeacherIdOrderByCreatedAtDesc(teacher.getId())
                .stream().map(this::mapBookingToResponse).collect(Collectors.toList());
    }

    @Transactional
    public BookingDto.BookingResponse updateBookingStatus(Long bookingId, Booking.BookingStatus newStatus) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(newStatus);
        booking = bookingRepository.save(booking);
        return mapBookingToResponse(booking);
    }

    public Booking findByRazorpayOrderId(String orderId) {
        return bookingRepository.findByRazorpayOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Booking not found for order: " + orderId));
    }

    @Transactional
    public BookingDto.BookingResponse markAsCompleted(Long studentId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getStudent().getId().equals(studentId)) {
            throw new RuntimeException("You can only mark your own bookings as completed");
        }

        if (booking.getStatus() != Booking.BookingStatus.CONFIRMED) {
            throw new RuntimeException("Only confirmed bookings can be marked as completed");
        }

        booking.setStatus(Booking.BookingStatus.COMPLETED);
        booking = bookingRepository.save(booking);
        return mapBookingToResponse(booking);
    }

    @Transactional
    public BookingDto.BookingResponse confirmBooking(Long teacherUserId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        TeacherProfile teacher = teacherRepository.findByUserId(teacherUserId)
                .orElseThrow(() -> new RuntimeException("Teacher profile not found"));

        if (!booking.getTeacher().getId().equals(teacher.getId())) {
            throw new RuntimeException("You can only confirm your own bookings");
        }

        if (booking.getStatus() != Booking.BookingStatus.PENDING) {
            throw new RuntimeException("Only pending bookings can be confirmed");
        }

        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        booking = bookingRepository.save(booking);

        // Increment teacher's total bookings
        teacher.setTotalBookings(teacher.getTotalBookings() + 1);
        teacherRepository.save(teacher);

        return mapBookingToResponse(booking);
    }

    @Transactional
    public BookingDto.BookingResponse rejectBooking(Long teacherUserId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        TeacherProfile teacher = teacherRepository.findByUserId(teacherUserId)
                .orElseThrow(() -> new RuntimeException("Teacher profile not found"));

        if (!booking.getTeacher().getId().equals(teacher.getId())) {
            throw new RuntimeException("You can only reject your own bookings");
        }

        if (booking.getStatus() != Booking.BookingStatus.PENDING) {
            throw new RuntimeException("Only pending bookings can be rejected");
        }

        booking.setStatus(Booking.BookingStatus.CANCELLED);
        booking = bookingRepository.save(booking);
        return mapBookingToResponse(booking);
    }

    // --- Mappers ---

    private BookingDto.SlotResponse mapSlotToResponse(AvailabilitySlot slot, boolean isBooked) {
        BookingDto.SlotResponse response = new BookingDto.SlotResponse();
        response.setId(slot.getId());
        response.setDayOfWeek(slot.getDayOfWeek());
        response.setStartTime(slot.getStartTime());
        response.setEndTime(slot.getEndTime());
        response.setBooked(isBooked);
        return response;
    }

    private BookingDto.BookingResponse mapBookingToResponse(Booking booking) {
        BookingDto.BookingResponse response = new BookingDto.BookingResponse();
        response.setId(booking.getId());
        response.setTeacherName(booking.getTeacher().getUser().getName());
        response.setStudentName(booking.getStudent().getName());
        response.setBookingDate(booking.getBookingDate());
        response.setStartTime(booking.getSlot().getStartTime());
        response.setEndTime(booking.getSlot().getEndTime());
        response.setStatus(booking.getStatus().name());
        response.setRazorpayOrderId(booking.getRazorpayOrderId());
        response.setAmount(booking.getAmount() != null ? booking.getAmount().doubleValue() : null);
        return response;
    }
}
