package com.example.tracking_orderad.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductReviewReq {
    private String productId;
    private Integer rating;
    private String comment;
}
