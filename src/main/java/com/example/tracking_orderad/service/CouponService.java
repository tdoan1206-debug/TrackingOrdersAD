package com.example.tracking_orderad.service;

import java.math.BigDecimal;

public interface CouponService {
    BigDecimal calculateCoupon(String couponCode, BigDecimal subtotal); // can dung doi voi PERCENT

    void increaseUsedCount(String couponCode);

}
