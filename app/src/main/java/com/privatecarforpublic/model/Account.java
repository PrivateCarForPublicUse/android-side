package com.privatecarforpublic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Account {
    private Long id;
    private String password;
    private String token;
    private Integer flag;//1:管理员 0：用户
}
