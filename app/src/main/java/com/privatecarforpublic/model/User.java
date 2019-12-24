package com.privatecarforpublic.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User implements Serializable {
        private Long id;
        //用户名
        private String userName;
        //账号
        private Long accountId;
        //工号
        private Long workNumber;
        //姓名
        private String name;
        //性别，0为男，1为女
        private int gender;
        //手机号
        private String phoneNumber;
        //企业id
        private Long companyId;
        //头像url
        private String headPhotoUrl;
        //账户余额
        private double remainMoney;
        //身份证号
        private String idCardNumber;
        //身份证正面url
        private String idCardFrontUrl;
        //身份证反面url
        private String idCardBackUrl;
        //身份证到期日期
        private String idCardDueDate;
        //驾驶证号
        private String driverLicenseNumber;
        //驾驶证正面url
        private String driverLicenseFrontUrl;
        //驾驶证反面url
        private String driverLicenseBackUrl;
        //驾驶证到期日期
        private String driverLicenseDueDate;
        //驾驶员星级
        private double starLevel;
        //审核状态,-1审核不通过,0未审核,1审核通过
        private int checkStatus;
        //用户设备id
        private Long tid;
        //是否删除
        private int isDeleted;

        public User(){
                this.name="张三";
                this.starLevel=2.5;
                this.tid=(long)233520958;
        }
}
