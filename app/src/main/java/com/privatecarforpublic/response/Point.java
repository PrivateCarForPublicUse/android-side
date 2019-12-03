package com.privatecarforpublic.response;

import java.io.Serializable;

public class Point implements Serializable {
    private String _param_err_info;
    private String _err_point_index;
    private int accuracy;
    private String location;
    private int speed;
    private long locatetime;
    private int direction;
    private int height;

    public String get_param_err_info() {
        return _param_err_info;
    }

    public void set_param_err_info(String _param_err_info) {
        this._param_err_info = _param_err_info;
    }

    public String get_err_point_index() {
        return _err_point_index;
    }

    public void set_err_point_index(String _err_point_index) {
        this._err_point_index = _err_point_index;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public long getLocatetime() {
        return locatetime;
    }

    public void setLocatetime(long locatetime) {
        this.locatetime = locatetime;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
