package com.smartdownload.project.controller;


import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.smartdownload.project.entity.Order;
import com.smartdownload.project.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // 🔐 USER → Buy a project
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/buy/{projectId}")
    public ResponseEntity<Order> buyProject(
            @PathVariable Integer projectId,
            Principal principal
    ) {
        // principal.getName() → JWT లో ఉన్న email
        Order order = orderService.createOrder(projectId, principal.getName());
        return ResponseEntity.ok(order);
    }

    // 🔐 USER → View my orders
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my")
    public ResponseEntity<List<Order>> getMyOrders(Principal principal) {
        List<Order> orders = orderService.getOrdersByUser(principal.getName());
        return ResponseEntity.ok(orders);
    }

    // 🔐 ADMIN → View all orders
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
}
