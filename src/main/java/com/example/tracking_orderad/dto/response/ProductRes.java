package com.example.tracking_orderad.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRes {
    private String productId;

    private String productName;

    private BigDecimal basePrice;

    private String categoryName;

    private boolean inStock;
}
