package com.example.tracking_orderad.repository;

import com.example.tracking_orderad.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepo extends JpaRepository<Product, String> {

    @Query("""
    SELECT DISTINCT p
    FROM Product p
    JOIN FETCH p.productCategory
    LEFT JOIN FETCH p.productVariants pv
    LEFT JOIN FETCH pv.inventory
    """)
    List<Product> findAllProduct();

    @Query("""
    SELECT p
    FROM Product p
    JOIN FETCH p.productCategory
    WHERE p.id = :id
    """)
    Optional<Product> findProductDetail(String id);
}
