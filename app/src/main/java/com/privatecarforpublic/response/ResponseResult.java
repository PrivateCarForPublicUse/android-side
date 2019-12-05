package com.privatecarforpublic.response;

import java.io.Serializable;

public class ResponseResult implements Serializable {
    private static final long serialVersionUID = 4832771715671880043L;
    private Integer code;
    private String msg;
    private String data;

    public ResponseResult() {
    }

    public ResponseResult(Integer code, String msg, String data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }
}