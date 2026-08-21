package com.example.tracking_orderad.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCartReq {
    @NotBlank(message = "variant is not empty")
    private String productVariantId;

    @NotNull(message = "quantity is not empty")
    @Min(value = 0, message = "quantity is greater or equal 0")
    private Integer quantity;
}
