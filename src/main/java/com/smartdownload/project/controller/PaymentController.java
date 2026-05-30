package com.smartdownload.project.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.smartdownload.project.entity.Payment;
import com.smartdownload.project.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    /**
     * 🔥 STEP 1: Create Razorpay Order
     * FRONTEND → Buy button click చేసినప్పుడు call చేస్తుంది
     */
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create")
    public ResponseEntity<?> createPaymentOrder(
            @RequestParam Integer orderId,
            @RequestParam Double amount
    ) {

        Payment payment = paymentService.createRazorpayOrder(orderId, amount);

        // Frontend కు Razorpay orderId పంపాలి
        return ResponseEntity.ok(
            Map.of(
                "razorpayOrderId", payment.getRazorpayOrderId(),
                "amount", payment.getAmount()
            )
        );
    }

    /**
     * 🔥 STEP 2: Verify Payment (after Razorpay success)
     * Razorpay popup success అయిన తర్వాత frontend ఇది call చేస్తుంది
     */
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody Map<String, String> payload) {

        String razorpayOrderId   = payload.get("razorpay_order_id");
        String razorpayPaymentId = payload.get("razorpay_payment_id");
        String razorpaySignature = payload.get("razorpay_signature");

        Payment payment = paymentService.verifyPayment(
                razorpayOrderId,
                razorpayPaymentId,
                razorpaySignature
        );

        return ResponseEntity.ok(
            Map.of(
                "message", "Payment Successful ✅",
                "paymentStatus", payment.getPaymentStatus()
            )
        );
    }
}
