package com.example.tracking_orderad.service;

import com.example.tracking_orderad.dto.request.CreateProductRequest;
import com.example.tracking_orderad.dto.request.CreateVariantRequest;
import com.example.tracking_orderad.dto.response.CreateProductResponse;
import com.example.tracking_orderad.dto.response.CreateVariantResponse;
import com.example.tracking_orderad.dto.response.ProductDetailRes;
import com.example.tracking_orderad.dto.response.ProductRes;

import java.util.List;

public interface ProductService {
    List<ProductRes> getAll();
    List<ProductDetailRes> getAllAdmin();

    ProductDetailRes getById(String id);

    CreateProductResponse createProduct(CreateProductRequest request) ;

    CreateVariantResponse createVariant(CreateVariantRequest request) ;
}
