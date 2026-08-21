package com.example.tracking_orderad.dto.request;

import com.example.tracking_orderad.common.OriginType;
import com.example.tracking_orderad.common.ReasonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateReturnReq {
    private String orderId;
    private ReasonEnum reason;
    private OriginType originType;
    private String notes;
}
