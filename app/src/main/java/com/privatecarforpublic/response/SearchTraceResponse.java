package com.privatecarforpublic.response;

import java.io.Serializable;
import java.util.List;

public class SearchTraceResponse implements Serializable {
    private Data data;
    private long errcode;
    private String errdetail;
    private String errmsg;
    private class Data{
        long counts;
        List<Track> tracks;

        public long getCounts() {
            return counts;
        }

        public void setCounts(long counts) {
            this.counts = counts;
        }

        public List<Track> getTracks() {
            return tracks;
        }

        public void setTracks(List<Track> tracks) {
            this.tracks = tracks;
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
