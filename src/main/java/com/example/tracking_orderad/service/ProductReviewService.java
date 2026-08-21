package com.example.tracking_orderad.service;

import com.example.tracking_orderad.dto.request.CreateProductReviewReq;
import com.example.tracking_orderad.dto.response.ProductReviewRes;

import java.util.List;

public interface ProductReviewService {
    ProductReviewRes createReview(String userId, CreateProductReviewReq req);

    List<ProductReviewRes> getReviewsByProduct(String productId);

    List<ProductReviewRes> getReviewsByUser(String userId);
}
