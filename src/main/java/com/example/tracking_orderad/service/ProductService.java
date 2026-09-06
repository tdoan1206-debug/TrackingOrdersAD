package com.example.tracking_orderad.service;

import com.example.tracking_orderad.dto.request.CreateProductRequest;
import com.example.tracking_orderad.dto.request.CreateVariantRequest;
import com.example.tracking_orderad.dto.response.*;

import com.example.tracking_orderad.entity.ProductCategory;

import java.util.List;

public interface ProductService {
    List<ProductRes> getAll();
    List<ProductDetailRes> getAllAdmin();

    ProductDetailRes getById(String id);

    CreateProductResponse createProduct(CreateProductRequest request) ;

    CreateVariantResponse createVariant(CreateVariantRequest request) ;

    List<ProductCategoryRes> getCategories();
}
