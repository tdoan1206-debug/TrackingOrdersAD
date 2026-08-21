package com.example.tracking_orderad.repository;

import com.example.tracking_orderad.entity.Cart;
import com.example.tracking_orderad.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart, String> {
    //Lấy ra giỏ hàng của User
    Optional<Cart> findByUser(User user);
}
