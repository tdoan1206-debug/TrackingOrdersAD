package com.example.tracking_orderad.dto.request;

import com.example.tracking_orderad.common.PaymentType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaceOrderReq {
    @Valid
    @NotEmpty(message = "required order summary")
    private List<OrderSummaryItemReq> items;

    @NotBlank
    private String addressId;

    private String couponCode;

    @NotNull(message = "paymentType is required")
    private PaymentType paymentType;
}
