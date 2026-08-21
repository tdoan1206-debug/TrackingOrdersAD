package com.example.tracking_orderad.service;

import com.example.tracking_orderad.dto.request.AddToCartReq;
import com.example.tracking_orderad.dto.request.UpdateCartReq;
import com.example.tracking_orderad.dto.response.CartRes;

public interface CartService {
    CartRes getCurrentCart();

    CartRes addToCart(AddToCartReq req);

    CartRes updateCartItem(UpdateCartReq req);
}
