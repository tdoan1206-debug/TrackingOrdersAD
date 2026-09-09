package com.example.tracking_orderad.dto.response;

import com.example.tracking_orderad.common.DiscountTypeEnum;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CouponsResponse {
    private String id ;
    private DiscountTypeEnum couponType;
    private BigDecimal value ;
}

