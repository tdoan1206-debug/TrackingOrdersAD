package com.example.tracking_orderad.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateVariantRequest {

    private String variantId;

    private String variantName;

    private String sku;

    private BigDecimal priceModifier;

    private Integer quantityInStock;
}