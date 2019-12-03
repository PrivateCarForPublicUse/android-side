package com.privatecarforpublic.response;

import java.io.Serializable;

public class AddTraceResponse implements Serializable {
    private Data data;
    private long errcode;
    private String errdetail;
    private String errmsg;
    private class Data{
        long trid;

        public long getTrid() {
            return trid;
        }

        public void setTrid(long trid) {
            this.trid = trid;
        }
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public long getErrcode() {
        return errcode;
    }

    public void setErrcode(long errcode) {
        this.errcode = errcode;
    }

    public String getErrdetail() {
        return errdetail;
    }

    public void setErrdetail(String errdetail) {
        this.errdetail = errdetail;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
