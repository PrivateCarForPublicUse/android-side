package com.privatecarforpublic.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Car implements Serializable {
    private Long id;
    //车辆id
    private String license;
    // 牌照
    private String picture;
    //车辆照片url
    private String drivingLicenseUrl;
    //车辆行驶证url
    private String brand;
    //车辆品牌
    private String type;
    //车辆型号
    private double journey;
    //以行驶的路程
    private double starOfCar;
    //车辆星级
    private String insuranceCompany;
    //保险公司
    private String strongInsurancePolicy;
    //交强险保单
    private String commercialInsurancePolicy;
    //商业险保单
    private Long userId;
    //车主id
    private int isPublic;
    //公私状态 （1 公车；0 私车）
    private int isUse;
    //使用状态（0 空闲；1 审核中；2 使用中 ）
    private String starTime;
    //车辆公用开始时间
    private String endTime;
    //车辆公用结束时间
    private double displacement;
    //车辆排量

    public Car(){
        this.license="浙BE693F";
        this.type="奥迪A6";
        this.starOfCar=(double)3.6;
    }
}
