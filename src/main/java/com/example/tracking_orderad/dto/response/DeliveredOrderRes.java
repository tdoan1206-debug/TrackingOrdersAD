package com.example.tracking_orderad.dto.response;

import com.example.tracking_orderad.common.OrderStatusEnum;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveredOrderRes {
    private String orderId;
    private OrderStatusEnum status;
    private String message;
}
