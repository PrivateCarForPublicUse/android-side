package com.privatecarforpublic.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Master implements Serializable {
    private Long id;
    //管理员用户名
    private String name;
    //管理员密码
    private String password;
    //是否为企业管理员，0：不是 1：是
    private int isCompanyMaster;
    //对应的企业id
    private Long companyId;
}
