package com.privatecarforpublic.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Apply implements Serializable {
    private User user;
    private Car car;
    private Long id;
    //申请开始时间
    private String applyStartTime;
    //申请结束时间
    private String applyEndTime;
    //申请状态 （-1 审核不通过；0 未审核；1 审核通过；2 行程中；3 已完成；4 已取消）
    private int status;
    //申请理由
    private String reason;
    //段行程
    private List<SecRoute> secRouteList;
}
