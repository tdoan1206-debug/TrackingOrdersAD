package com.example.tracking_orderad.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductVariantRes {
    private String id;
    private String name;
    private String sku;
    private BigDecimal priceModifier;
    private Integer quantityInStock;
}
