package com.example.tracking_orderad.dto.response;

import com.example.tracking_orderad.common.OrderStatusEnum;
import com.example.tracking_orderad.common.PaymentType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellerOrderDetailRes {
    private String orderId;

    private String trackingNumber;

    private OrderStatusEnum status;

    private PaymentType paymentType;

    private BigDecimal subtotal;

    private BigDecimal discountAmount;

    private BigDecimal shippingFee;

    private BigDecimal grandTotal;

    private Date estimatedDeliveryDate;

    // Buyer
    private String buyerUsername;
    private String buyerPhone;

    // Shipping Address
    private String recipientName;
    private String recipientPhone;
    private String province;
    private String city;
    private String district;
    private String detailAddress;

    // Carrier
    private String carrierName;

    private List<SellerOrderItemRes> items;

    private List<TrackingHistoryRes> trackingLogs;
    private LocalDateTime createdAt;
}
