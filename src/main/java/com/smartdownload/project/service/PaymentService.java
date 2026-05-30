package com.smartdownload.project.service;

import java.time.LocalDateTime;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.smartdownload.project.entity.Payment;
import com.smartdownload.project.entity.Payment.PaymentStatus;
import com.smartdownload.project.repository.PaymentRepository;

@Service
public class PaymentService {

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderService orderService;   // 🔥 NEW

    /**
     * 🔥 STEP 1: Create Razorpay Order
     */
    public Payment createRazorpayOrder(Integer orderId, Double amount) {

        try {
            RazorpayClient client =
                    new RazorpayClient(razorpayKeyId, razorpayKeySecret);

            JSONObject options = new JSONObject();
            options.put("amount", (int) (amount * 100)); // paise
            options.put("currency", "INR");
            options.put("receipt", "order_rcpt_" + orderId);

            Order razorpayOrder = client.orders.create(options);

            Payment payment = new Payment();
            payment.setOrderId(orderId);
            payment.setRazorpayOrderId(razorpayOrder.get("id"));
            payment.setAmount(amount);

            return paymentRepository.save(payment);

        } catch (Exception e) {
            throw new RuntimeException("Razorpay order creation failed", e);
        }
    }

    /**
     * 🔥 STEP 2: Verify Payment
     */
    public Payment verifyPayment(
            String razorpayOrderId,
            String razorpayPaymentId,
            String razorpaySignature
    ) {

        Payment payment = paymentRepository
                .findByRazorpayOrderId(razorpayOrderId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setRazorpayPaymentId(razorpayPaymentId);
        payment.setRazorpaySignature(razorpaySignature);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setPaidAt(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);

        // 🔥 CRITICAL: Update Order Status also
        orderService.updateOrderStatus(
                savedPayment.getOrderId(),
                com.smartdownload.project.entity.Order.OrderStatus.PAID
        );

        return savedPayment;
    }
}
