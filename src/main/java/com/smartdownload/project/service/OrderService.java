package com.smartdownload.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartdownload.project.entity.Order;
import com.smartdownload.project.entity.User;
import com.smartdownload.project.repository.OrderRepository;
import com.smartdownload.project.repository.UserRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    // 🛒 CREATE ORDER
    public Order createOrder(Integer projectId, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setUserId(user.getUserId());
        order.setProjectId(projectId);
        order.setTotalAmount(0.0);
        order.setOrderStatus(Order.OrderStatus.CREATED);

        return orderRepository.save(order);
    }

    // 📦 USER ORDERS (FIXED)
    public List<Order> getOrdersByUser(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ CORRECT METHOD
        return orderRepository.findByUserId(user.getUserId());
    }

    // 🛠 ADMIN → ALL ORDERS
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // 💰 UPDATE ORDER STATUS
    public Order updateOrderStatus(Integer orderId, Order.OrderStatus status) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setOrderStatus(status);

        return orderRepository.save(order);
    }

    // 🔐 PURCHASE CHECK — 100% CORRECT
    public boolean hasUserPurchased(Integer userId, Integer projectId) {

        return orderRepository.hasPaidOrder(userId, projectId);
    }
}
