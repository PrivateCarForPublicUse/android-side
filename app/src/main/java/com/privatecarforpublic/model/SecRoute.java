package com.privatecarforpublic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SecRoute {
    private Long id;
    //行程表id
    private Long routeId;
    //出发点
    private String origin;
    //目的地
    private String destination;
    //出发地经度
    private String oriLongitude;
    //出发地纬度
    private String oriLatitude;
    //目的地经度
    private String desLongitude;
    //目的地纬度
    private String desLatitude;
}
