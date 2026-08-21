package com.example.tracking_orderad.configmapper;

import com.example.tracking_orderad.dto.response.ProductReviewRes;
import com.example.tracking_orderad.entity.ProductReview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductReviewMapper {

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.username")
    ProductReviewRes toProductReviewRes(ProductReview review);

    List<ProductReviewRes> toProductReviewResList(List<ProductReview> reviews);
}
