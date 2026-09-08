package com.example.tracking_orderad.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class UpdateProductRequest {

    private String productName;

    private String categoryId;

    private String description;

    private int weightGram;

    private BigDecimal basePrice;

    private List<UpdateVariantRequest> variants;
}