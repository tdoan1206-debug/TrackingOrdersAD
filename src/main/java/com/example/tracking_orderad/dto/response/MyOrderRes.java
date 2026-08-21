package com.example.tracking_orderad.dto.response;

import com.example.tracking_orderad.common.OrderStatusEnum;
import com.example.tracking_orderad.common.PaymentType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Builder
public class MyOrderRes {
    private String orderId;
    private String trackingNumber;
    private Date createdAt;
    private PaymentType paymentType;
    private Integer totalItems;
    private BigDecimal grandTotal;
    private OrderStatusEnum status;

}
