package com.example.tracking_orderad.service;

import com.example.tracking_orderad.dto.response.ProductDetailRes;
import com.example.tracking_orderad.dto.response.ProductRes;

import java.util.List;

public interface ProductService {
    List<ProductRes> getAll();
    List<ProductDetailRes> getAllAdmin();

    ProductDetailRes getById(String id);
}
