package com.example.tracking_orderad.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BuyNowRes {
    private String productVariantId;

    private Integer quantity;

    private String message;

}
