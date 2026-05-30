package com.smartdownload.project.dto;

import java.time.LocalDateTime;

import com.smartdownload.project.entity.Order.OrderStatus;

public class OrderResponse {

    private Integer orderId;
    private Integer projectId;
    private Double totalAmount;
    private OrderStatus status;
    private LocalDateTime createdAt;

    public OrderResponse() {
    }

    public OrderResponse(
            Integer orderId,
            Integer projectId,
            Double totalAmount,
            OrderStatus status,
            LocalDateTime createdAt) {

        this.orderId = orderId;
        this.projectId = projectId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
