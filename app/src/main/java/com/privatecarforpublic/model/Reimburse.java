package com.privatecarforpublic.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @date:2019/12/7
 * @author:zhongcz
 */
@Data
@AllArgsConstructor
public class Reimburse implements Serializable {
    //private Long id;
    private int imgId;
    /*// 车的照片Url
    private String imgUrl;*/
    private String time;
    private String start;
    private String end;
    private String status;
    private double amount;

}
