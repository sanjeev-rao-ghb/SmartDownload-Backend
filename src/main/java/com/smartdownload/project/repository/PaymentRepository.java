package com.smartdownload.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartdownload.project.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    // 🔍 Find payment by Razorpay Order ID
    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);

    // 🔍 Check if payment SUCCESS exists for an order
    boolean existsByOrderIdAndPaymentStatus(
            Integer orderId,
            Payment.PaymentStatus paymentStatus
    );
}
