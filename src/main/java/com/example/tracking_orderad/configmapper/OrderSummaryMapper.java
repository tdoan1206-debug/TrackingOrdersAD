package com.example.tracking_orderad.configmapper;


import com.example.tracking_orderad.dto.response.OrderSummaryRes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface OrderSummaryMapper {

    @Mapping(source = "subTotal",target = "subTotal")
    @Mapping(source = "discountValue",target = "discountAmount")
    @Mapping(source = "shippingFee",target = "shippingFee")
    @Mapping(source = "grandTotal",target = "grandTotal")
    OrderSummaryRes toResponse (BigDecimal subTotal, BigDecimal discountValue, BigDecimal shippingFee , BigDecimal grandTotal) ;
}