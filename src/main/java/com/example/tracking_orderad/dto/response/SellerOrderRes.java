package com.example.tracking_orderad.dto.response;

import com.example.tracking_orderad.common.OrderStatusEnum;
import com.example.tracking_orderad.common.PaymentType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellerOrderRes {
    private String orderId;
    private String trackingNumber;
    private String buyerName;
    private OrderStatusEnum status;
    private PaymentType paymentMethod;
    private BigDecimal grandTotal;
    private Integer totalItems;
    private LocalDateTime createdAt;

}
