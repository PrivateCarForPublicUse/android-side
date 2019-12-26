package com.privatecarforpublic.model;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @date:2019/12/16
 * @author:zhongcz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyTravels implements Serializable {
    private Long id;
    //行程实际开始时间
    private String carStartTime;
    //行程实际结束时间
    private String carStopTime;
    //出发点
    private String origin;
    //目的地
    private String destination;
    //申请车辆id
    private Long carId;
    //申请员工id
    private Long userId;
    //申请状态 （-1 审核不通过；0 未审核；1 审核通过；2 行程中；3 已完成；4 已取消）
    private int status;
    //申请理由
    private String reason;
    //-1 报销失败；0 未报销；1 已报销；2 审核中
    private int isReimburse;
}
