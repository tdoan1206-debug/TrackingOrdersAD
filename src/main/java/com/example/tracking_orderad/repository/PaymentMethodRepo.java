package com.example.tracking_orderad.repository;

import com.example.tracking_orderad.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepo extends JpaRepository<PaymentMethod, String> {
}
