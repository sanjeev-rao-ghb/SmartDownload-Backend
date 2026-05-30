package com.smartdownload.project.dto;

public class AdminDashboardResponse {

    private long totalUsers;
    private long totalProjects;
    private long totalOrders;
    private long totalPayments;

    public AdminDashboardResponse(
            long totalUsers,
            long totalProjects,
            long totalOrders,
            long totalPayments) {

        this.totalUsers = totalUsers;
        this.totalProjects = totalProjects;
        this.totalOrders = totalOrders;
        this.totalPayments = totalPayments;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public long getTotalProjects() {
        return totalProjects;
    }

    public long getTotalOrders() {
        return totalOrders;
    }

    public long getTotalPayments() {
        return totalPayments;
    }
}
