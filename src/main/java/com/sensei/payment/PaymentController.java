package com.sensei.payment;

import com.sensei.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Step 1: Student initiates payment → gets Razorpay order details for checkout
     */
    @PostMapping("/initiate")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<PaymentDto.PaymentOrderResponse>> initiatePayment(
            @RequestBody PaymentDto.InitiatePaymentRequest request) {
        PaymentDto.PaymentOrderResponse response = paymentService.initiatePayment(request.getBookingId());
        return ResponseEntity.ok(ApiResponse.success("Payment order created", response));
    }

    /**
     * Step 2: Frontend sends back Razorpay response after checkout → verify signature
     */
    @PostMapping("/verify")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<String>> verifyPayment(@RequestBody PaymentDto.VerifyPaymentRequest request) {
        paymentService.verifyPayment(request);
        return ResponseEntity.ok(ApiResponse.success("Payment verified successfully", "CONFIRMED"));
    }

    /**
     * Webhook: Razorpay sends payment events here (backup confirmation)
     * This endpoint is PUBLIC (no auth) — verified by HMAC signature
     */
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("X-Razorpay-Signature") String signature) {
        paymentService.handleWebhook(payload, signature);
        return ResponseEntity.ok("OK");
    }
}
