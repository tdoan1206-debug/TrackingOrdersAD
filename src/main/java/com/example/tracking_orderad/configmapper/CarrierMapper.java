package com.example.tracking_orderad.configmapper;

import com.example.tracking_orderad.dto.request.CreateCarrierReq;
import com.example.tracking_orderad.dto.response.CarrierRes;
import com.example.tracking_orderad.entity.Carrier;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CarrierMapper {
    CarrierRes toCarrierRes(Carrier carrier);

    List<CarrierRes> toCarrierResList(List<Carrier> carriers);

    Carrier toCarrier(CreateCarrierReq req);
}
