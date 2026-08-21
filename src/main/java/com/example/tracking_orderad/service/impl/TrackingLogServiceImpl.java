package com.example.tracking_orderad.service.impl;

import com.example.tracking_orderad.config.basicauthconfig.AuthenticationFacade;
import com.example.tracking_orderad.configmapper.TrackingLogMapper;
import com.example.tracking_orderad.dto.response.TrackingHistoryRes;
import com.example.tracking_orderad.entity.TrackingLog;
import com.example.tracking_orderad.entity.User;
import com.example.tracking_orderad.exception.NotFoundException;
import com.example.tracking_orderad.repository.OrderRepo;
import com.example.tracking_orderad.repository.TrackingLogRepo;
import com.example.tracking_orderad.repository.UserRepo;
import com.example.tracking_orderad.service.TrackingLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrackingLogServiceImpl implements TrackingLogService {
    private final UserRepo userRepo;
    private final TrackingLogRepo trackingLogRepo;
    private final AuthenticationFacade authenticationFacade;
    private final OrderRepo orderRepo;
    private final TrackingLogMapper trackingLogMapper;

    @Override
    @Transactional(readOnly = true)
    public List<TrackingHistoryRes> getTrackingHistory(String orderId) {
        User user = authenticationFacade.getCurrentUser();

        // check don hang co thuoc user
        orderRepo.findByIdAndUser(orderId, user)
                .orElseThrow(() ->
                        new NotFoundException(HttpStatus.NOT_FOUND, "Order Not Found"));

        // Lay ra tracking history
        List<TrackingLog> trackingLogs = trackingLogRepo.findByOrderId(orderId);
        log.info("Found {} tracking logs for user {}", trackingLogs.size(), user);

        return trackingLogMapper.toTrackingHistoryResList(trackingLogs);
    }
}
