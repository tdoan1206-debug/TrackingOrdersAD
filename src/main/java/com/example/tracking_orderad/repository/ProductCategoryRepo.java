package com.example.tracking_orderad.repository;

import com.example.tracking_orderad.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProductCategoryRepo extends JpaRepository<ProductCategory,String>, JpaSpecificationExecutor<ProductCategory> {
    Optional<ProductCategory> findById(String id);
}
