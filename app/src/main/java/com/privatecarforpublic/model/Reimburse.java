package com.privatecarforpublic.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @date:2019/12/7
 * @author:zhongcz
 */
@Data
@AllArgsConstructor
public class Reimburse {
    //private Long id;
    private int imgId;
    private String time;
    private String start;
    private String end;
    private String status;
    private double amount;

}
