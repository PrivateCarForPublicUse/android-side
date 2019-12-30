package com.privatecarforpublic.model;

import android.support.v4.app.NotificationCompat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @date:2019/12/29
 * @author:zhongcz
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyTravelsUtil {
    //行程实际开始时间
    private String carStartTime;
    //出发点
    private String origin;
    //目的地
    private String destination;
    //-1 报销失败；0 未报销；1 已报销；2 审核中
    private int isReimburse;
    //申请状态 （-1 审核不通过；0 未审核；1 审核通过；2 行程中；3 已完成；4 已取消）
    private int status;
}
