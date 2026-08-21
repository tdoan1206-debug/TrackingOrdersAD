package com.example.tracking_orderad.service.impl;

import com.example.tracking_orderad.dto.response.*;
import com.example.tracking_orderad.service.CarrierOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CarrierOrderServiceImpl implements CarrierOrderService {
    @Override
    public ShippingOrderRes shippingOrder(String orderId) {
        return null;
    }

    @Override
    public DeliveredOrderRes deliveredOrder(String orderId) {
        return null;
    }

    @Override
    public FailedOrderRes failedOrder(String orderId) {
        return null;
    }

    @Override
    public ReturningOrderRes returningOrder(String orderId) {
        return null;
    }

    @Override
    public ReattemptOrderRes reattemptOrder(String orderId) {
        return null;
    }
}
