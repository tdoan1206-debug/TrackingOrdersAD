package com.example.tracking_orderad.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartRes {
    private List<CartItemRes> items;

}
