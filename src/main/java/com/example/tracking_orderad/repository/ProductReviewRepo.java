package com.example.tracking_orderad.repository;

import com.example.tracking_orderad.entity.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductReviewRepo extends JpaRepository<ProductReview, String> {
    List<ProductReview> findByProductId(String productId);
    List<ProductReview> findByUserId(String userId);
}
