package com.example.tracking_orderad.service;

import com.example.tracking_orderad.dto.request.CreateCarrierReq;
import com.example.tracking_orderad.dto.request.UpdateCarrierReq;
import com.example.tracking_orderad.dto.response.CarrierRes;
import com.example.tracking_orderad.dto.response.CreateCarrierRes;
import com.example.tracking_orderad.dto.response.ShipperRes;

import java.util.List;

public interface CarrierService {
    List<CarrierRes> getAll();

    CarrierRes getById(String id);

    CreateCarrierRes createCarrier(CreateCarrierReq req);

    CarrierRes updateCarrier(String id,UpdateCarrierReq req);

    void active(String id);
    void inactive(String id);

    List<ShipperRes> getShippersByCarrier(String carrierId);

}
