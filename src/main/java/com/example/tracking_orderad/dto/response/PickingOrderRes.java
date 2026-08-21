package com.example.tracking_orderad.dto.response;

import com.example.tracking_orderad.common.OrderStatusEnum;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PickingOrderRes {
    private String orderId;
    private OrderStatusEnum status;
    private String message;
}
