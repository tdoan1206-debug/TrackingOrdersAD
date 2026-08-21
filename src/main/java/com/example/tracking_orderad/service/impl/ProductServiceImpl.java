package com.example.tracking_orderad.service.impl;

import com.example.tracking_orderad.configmapper.ProductMapper;
import com.example.tracking_orderad.configmapper.ProductVariantMapper;
import com.example.tracking_orderad.dto.response.ProductDetailRes;
import com.example.tracking_orderad.dto.response.ProductRes;
import com.example.tracking_orderad.entity.Product;
import com.example.tracking_orderad.exception.NotFoundException;
import com.example.tracking_orderad.repository.ProductRepo;
import com.example.tracking_orderad.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;
    private final ProductMapper productMapper;
    private final ProductVariantMapper productVariantMapper;


    @Override
    @Transactional(readOnly = true)
    public List<ProductRes> getAll() {
        List<Product> products = productRepo.findAllProduct();
        return productMapper.toProductResList(products);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDetailRes> getAllAdmin() {
        List<Product> products = productRepo.findAllProduct();
        return products.stream().map(product -> {
            ProductDetailRes res = productMapper.toProductDetailRes(product);
            res.setVariants(productVariantMapper.toProductVariantResList(product.getProductVariants()));
            return res;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDetailRes getById(String id) {
        Product product = productRepo.findProductDetail(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                HttpStatus.NOT_FOUND,
                                "Product not found"));

        ProductDetailRes res = productMapper.toProductDetailRes(product);
        // Gắn danh sách variants vào response để frontend biết productVariantId
        res.setVariants(productVariantMapper.toProductVariantResList(product.getProductVariants()));
        return res;
    }
}
