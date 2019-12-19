package com.privatecarforpublic.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @date:2019/12/16
 * @author:zhongcz
 */
@Data
@AllArgsConstructor
public class MyTravels {
    private Date date;

    private String start;

    private String end;

    private boolean finish_status;

    private boolean reim_status;
}
