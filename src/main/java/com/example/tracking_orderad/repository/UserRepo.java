package com.example.tracking_orderad.repository;

import com.example.tracking_orderad.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
}
