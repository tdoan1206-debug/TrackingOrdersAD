package com.example.tracking_orderad.service;

import com.example.tracking_orderad.dto.response.TrackingHistoryRes;

import java.util.List;

public interface TrackingLogService {
    List<TrackingHistoryRes> getTrackingHistory(String orderId);
}
