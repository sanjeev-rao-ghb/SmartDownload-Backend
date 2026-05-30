package com.smartdownload.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smartdownload.project.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    // ✅ USER WISE ORDERS
    List<Order> findByUserId(Integer userId);

    // ✅ CORRECT PURCHASE CHECK
    @Query("""
      SELECT COUNT(o) > 0
      FROM Order o
      WHERE o.userId = :userId
      AND o.projectId = :projectId
      AND o.orderStatus = 'PAID'
    """)
    boolean hasPaidOrder(
        @Param("userId") Integer userId,
        @Param("projectId") Integer projectId
    );
}
