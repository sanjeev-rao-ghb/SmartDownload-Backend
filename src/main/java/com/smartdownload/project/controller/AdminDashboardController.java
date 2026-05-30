package com.smartdownload.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartdownload.project.dto.AdminDashboardResponse;
import com.smartdownload.project.entity.Order;
import com.smartdownload.project.entity.Payment;
import com.smartdownload.project.entity.User;
import com.smartdownload.project.repository.OrderRepository;
import com.smartdownload.project.repository.PaymentRepository;
import com.smartdownload.project.repository.ProjectRepository;
import com.smartdownload.project.repository.UserRepository;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ProjectRepository projectRepo;

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private PaymentRepository paymentRepo;

    // ==========================
    // ADMIN DASHBOARD STATS
    // ==========================
    @GetMapping("/dashboard")
    public AdminDashboardResponse dashboard() {

        return new AdminDashboardResponse(
            userRepo.count(),
            projectRepo.count(),
            orderRepo.count(),
            paymentRepo.count()
        );
    }

    // ==========================
    // GET ALL USERS
    // ==========================
    @GetMapping("/users")
    public List<User> users() {
        return userRepo.findAll();
    }

    // ==========================
    // GET ALL ORDERS
    // ==========================
    @GetMapping("/orders")
    public List<Order> orders() {
        return orderRepo.findAll();
    }

    // ==========================
    // GET ALL PAYMENTS
    // ==========================
    @GetMapping("/payments")
    public List<Payment> payments() {
        return paymentRepo.findAll();
    }
}
