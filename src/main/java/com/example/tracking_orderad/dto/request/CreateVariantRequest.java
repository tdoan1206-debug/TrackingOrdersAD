package com.example.tracking_orderad.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateVariantRequest {
    private String productId ;

    private String variantName ;

    @NotBlank(message = "SKU không được trống")
    @Size(max = 100)
    private String sku;

    private BigDecimal priceModifier;

    private int stock ;

}