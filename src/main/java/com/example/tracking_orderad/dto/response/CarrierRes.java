package com.example.tracking_orderad.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CarrierRes {
    private String id;

    private String name;

    private String apiEndpoint;

    private String supportRegions;

    private Boolean isActive;
}
