package com.example.tracking_orderad.service;

import com.example.tracking_orderad.dto.request.CreateReturnReq;
import com.example.tracking_orderad.dto.response.ReturnRes;

import java.util.List;

public interface ReturnService {
    ReturnRes createReturn(CreateReturnReq req);
    List<ReturnRes> getReturnsByUser(String userId);
    List<ReturnRes> getReturnsByOrder(String orderId);
    List<ReturnRes> getAllReturns();
    ReturnRes updateReturnStatus(String returnId, String status);
}
