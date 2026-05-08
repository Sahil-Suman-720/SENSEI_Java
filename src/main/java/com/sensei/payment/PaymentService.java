package com.sensei.payment;

import com.sensei.booking.Booking;
import com.sensei.booking.BookingRepository;
import com.sensei.booking.BookingService;
import com.sensei.teacher.TeacherProfile;
import com.sensei.teacher.TeacherRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Service
@RequiredArgsConstructor
public class PaymentService {

    @Value("${razorpay.key-id}")
    private String razorpayKeyId;

    @Value("${razorpay.key-secret}")
    private String razorpayKeySecret;

    @Value("${razorpay.webhook-secret}")
    private String webhookSecret;

    private final BookingRepository bookingRepository;
    private final TeacherRepository teacherRepository;

    /**
     * Creates a Razorpay order for a booking.
     * Frontend will use this order to open Razorpay checkout.
     */
    @Transactional
    public PaymentDto.PaymentOrderResponse initiatePayment(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStatus() != Booking.BookingStatus.PENDING) {
            throw new RuntimeException("Payment can only be initiated for PENDING bookings");
        }

        try {
            RazorpayClient razorpay = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

            // Amount in paise (1 INR = 100 paise)
            long amountInPaise = booking.getAmount().longValue() * 100;

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amountInPaise);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "booking_" + booking.getId());

            Order order = razorpay.orders.create(orderRequest);

            // Save Razorpay order ID to booking
            booking.setRazorpayOrderId(order.get("id"));
            booking.setStatus(Booking.BookingStatus.PAYMENT_INITIATED);
            bookingRepository.save(booking);

            // Return order details for frontend
            PaymentDto.PaymentOrderResponse response = new PaymentDto.PaymentOrderResponse();
            response.setRazorpayOrderId(order.get("id"));
            response.setAmount(amountInPaise);
            response.setCurrency("INR");
            response.setKeyId(razorpayKeyId);
            response.setBookingId(booking.getId());

            return response;

        } catch (RazorpayException e) {
            throw new RuntimeException("Failed to create Razorpay order: " + e.getMessage());
        }
    }

    /**
     * Verifies payment after frontend checkout completes.
     * Called by frontend with Razorpay's response data.
     */
    @Transactional
    public boolean verifyPayment(PaymentDto.VerifyPaymentRequest request) {
        // Verify signature: HMAC-SHA256(razorpayOrderId + "|" + razorpayPaymentId, secret)
        String payload = request.getRazorpayOrderId() + "|" + request.getRazorpayPaymentId();
        String expectedSignature = generateHmacSha256(payload, razorpayKeySecret);

        if (!expectedSignature.equals(request.getRazorpaySignature())) {
            throw new RuntimeException("Payment verification failed: invalid signature");
        }

        // Update booking status
        Booking booking = bookingRepository.findByRazorpayOrderId(request.getRazorpayOrderId())
                .orElseThrow(() -> new RuntimeException("Booking not found for this order"));

        booking.setRazorpayPaymentId(request.getRazorpayPaymentId());
        booking.setRazorpaySignature(request.getRazorpaySignature());
        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        bookingRepository.save(booking);

        // Increment teacher's total bookings
        TeacherProfile teacher = booking.getTeacher();
        teacher.setTotalBookings(teacher.getTotalBookings() + 1);
        teacherRepository.save(teacher);

        return true;
    }

    /**
     * Handles Razorpay webhook events (backup confirmation).
     * Razorpay sends this even if frontend verification fails.
     */
    @Transactional
    public void handleWebhook(String payload, String signature) {
        // Verify webhook signature
        String expectedSignature = generateHmacSha256(payload, webhookSecret);
        if (!expectedSignature.equals(signature)) {
            throw new RuntimeException("Invalid webhook signature");
        }

        // Parse and process (simplified — handle payment.captured event)
        JSONObject json = new JSONObject(payload);
        String event = json.getString("event");

        if ("payment.captured".equals(event)) {
            String orderId = json.getJSONObject("payload")
                    .getJSONObject("payment")
                    .getJSONObject("entity")
                    .getString("order_id");

            bookingRepository.findByRazorpayOrderId(orderId).ifPresent(booking -> {
                if (booking.getStatus() == Booking.BookingStatus.PAYMENT_INITIATED) {
                    booking.setStatus(Booking.BookingStatus.CONFIRMED);
                    bookingRepository.save(booking);
                }
            });
        }
    }

    private String generateHmacSha256(String data, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hash = mac.doFinal(data.getBytes());
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to generate HMAC signature", e);
        }
    }
}
