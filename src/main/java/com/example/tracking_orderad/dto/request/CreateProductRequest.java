package com.example.tracking_orderad.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CreateProductRequest {
    @NotBlank(message = "Tên sản phẩm không được trống")
    @Size(max = 500)
    private String productName;

    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal basePrice;

    private String description;

    @Min(0)
    private int weightGram;

    @NotBlank(message = "Danh mục không được trống")
    private String categoryId;


}