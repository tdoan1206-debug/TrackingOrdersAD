package com.example.tracking_orderad.repository;

import com.example.tracking_orderad.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepo extends JpaRepository<OrderItem, String> {
}
