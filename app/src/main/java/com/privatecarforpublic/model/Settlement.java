package com.privatecarforpublic.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Settlement implements Serializable {
    private Long id;
    //用户id
    private Long userId;
    //行程表id
    private Long routeId;
    //段行程id
    private Long secRouteId;
    //行程实际开始时间
    private String carStartTime;
    //行程实际结束时间
    private String carStopTime;
    //行驶路程（公里）
    private double drivingDistance;
    //行驶费用（元）
    private double drivingCost;
    //路径的traceId
    private String trid;

    private String routeUrl;
}
