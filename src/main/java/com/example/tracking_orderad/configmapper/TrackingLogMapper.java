package com.example.tracking_orderad.configmapper;

import com.example.tracking_orderad.dto.response.TrackingHistoryRes;
import com.example.tracking_orderad.entity.TrackingLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrackingLogMapper {
    @Mapping(source = "updateBy.username", target = "updatedBy")
    @Mapping(source = "timestamp", target = "updateAt")
    TrackingHistoryRes toTrackingHistoryRes(TrackingLog trackingLog);

    List<TrackingHistoryRes> toTrackingHistoryResList(List<TrackingLog> trackingLogs);
}
