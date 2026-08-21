package com.example.tracking_orderad.dto.response;

import com.example.tracking_orderad.common.OrderStatusEnum;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConfirmOrderRes {
    private String orderId;
    private OrderStatusEnum status;
    private String message;
}
