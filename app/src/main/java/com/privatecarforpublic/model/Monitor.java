package com.privatecarforpublic.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Monitor implements Serializable {
    private Long id;
    //申请开始时间
    private String applyStartTime;
    //申请结束时间
    private String applyEndTime;
    //申请车辆
    private Car car;
    //申请员工
    private User user;
    //申请状态 （-1 审核不通过；0 未审核；1 审核通过；2 行程中；3 已完成；4 已取消）
    private int status;
    //申请理由
    private String reason;

    public Monitor( Car car, User user, int status) {
        this.car = car;
        this.user = user;
        this.status = status;
    }
}
