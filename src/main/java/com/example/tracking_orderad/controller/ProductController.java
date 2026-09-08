package com.example.tracking_orderad.controller;

import com.example.tracking_orderad.dto.request.CreateProductRequest;
import com.example.tracking_orderad.dto.request.CreateVariantRequest;
import com.example.tracking_orderad.dto.request.UpdateProductRequest;
import com.example.tracking_orderad.dto.response.*;
import com.example.tracking_orderad.service.ProductService;
import com.example.tracking_orderad.entity.ProductCategory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    @PreAuthorize("hasAnyRole('BUYER', 'SELLER')")
    public ResponseEntity<List<ProductRes>> getAll() {
        return ResponseEntity.ok(productService.getAll());
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<List<ProductDetailRes>> getAllAdmin() {
        return ResponseEntity.ok(productService.getAllAdmin());
    }

    @GetMapping("/categories")
    @PreAuthorize("hasAnyRole('BUYER', 'SELLER')")
    public ResponseEntity<List<ProductCategoryRes>> getCategories() {
        return ResponseEntity.ok(productService.getCategories());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('BUYER', 'SELLER')")
    public ResponseEntity<ProductDetailRes> getById(@PathVariable String id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<CreateProductResponse> createProduct(@Valid @RequestBody CreateProductRequest request) {

        CreateProductResponse response = productService.createProduct(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/variants")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<CreateVariantResponse> createVariant(@Valid @RequestBody CreateVariantRequest request) {

        CreateVariantResponse response = productService.createVariant(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{productId}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<String> update(@Valid @RequestBody UpdateProductRequest request,
                                         @PathVariable String productId) {

        productService.update(request ,productId);
        return ResponseEntity.ok("Cập Nhật thành công");
    }


    // tất cả là tôi

}
