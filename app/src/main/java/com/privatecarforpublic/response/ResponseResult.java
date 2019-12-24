package com.privatecarforpublic.response;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.Serializable;

public class ResponseResult implements Serializable {
    private static final long serialVersionUID = 4832771715671880043L;
    private Integer code;
    private String message;
    private String data;

    public ResponseResult() {
    }

    public ResponseResult(Integer code, String message, String data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }
}