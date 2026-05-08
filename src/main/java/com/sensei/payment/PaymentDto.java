package com.sensei.payment;

import lombok.Data;

public class PaymentDto {

    @Data
    public static class InitiatePaymentRequest {
        private Long bookingId;
    }

    @Data
    public static class PaymentOrderResponse {
        private String razorpayOrderId;
        private Long amount; // in paise (INR smallest unit)
        private String currency;
        private String keyId; // Razorpay public key for frontend
        private Long bookingId;
    }

    @Data
    public static class VerifyPaymentRequest {
        private String razorpayOrderId;
        private String razorpayPaymentId;
        private String razorpaySignature;
    }

    @Data
    public static class WebhookPayload {
        private String event;
        private PayloadEntity payload;
    }

    @Data
    public static class PayloadEntity {
        private PaymentEntity payment;
    }

    @Data
    public static class PaymentEntity {
        private PaymentDetails entity;
    }

    @Data
    public static class PaymentDetails {
        private String id;
        private String order_id;
        private String status;
        private Long amount;
    }
}
