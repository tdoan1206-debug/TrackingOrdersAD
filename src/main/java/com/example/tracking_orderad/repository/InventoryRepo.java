package com.example.tracking_orderad.repository;

import com.example.tracking_orderad.entity.Inventory;
import com.example.tracking_orderad.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepo extends JpaRepository<Inventory, String> {
    // Tìm số lượng tồn kho theo variant
    Optional<Inventory> findByProductVariant(ProductVariant productVariant);
}
