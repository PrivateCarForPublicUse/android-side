package com.privatecarforpublic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecModel {
    private Long SecRouteId;
    private String origin;
    private String destination;
    private String carStartTime;
    private String carStopTime;
    private Double drivingDistance;
    private Double drivingCost;

}
