package com.example.tracking_orderad.configmapper;

import com.example.tracking_orderad.dto.response.ReturnRes;
import com.example.tracking_orderad.entity.Return;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReturnMapper {

    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "userId", source = "user.id")
    ReturnRes toReturnRes(Return returnEntity);

    List<ReturnRes> toReturnResList(List<Return> returnEntities);
}
