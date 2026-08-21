package com.example.tracking_orderad.service;

import com.example.tracking_orderad.dto.response.*;

public interface CarrierOrderService {

    ShippingOrderRes shippingOrder(String orderId);

    DeliveredOrderRes deliveredOrder(String orderId);

    FailedOrderRes failedOrder(String orderId);

    ReturningOrderRes returningOrder(String orderId);

    ReattemptOrderRes reattemptOrder(String orderId);
}
