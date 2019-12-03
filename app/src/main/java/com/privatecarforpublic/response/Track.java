package com.privatecarforpublic.response;

import java.io.Serializable;
import java.util.List;

public class Track implements Serializable {
    private long counts;
    private long distance;
    private List<Point> points;
    private long time;
    private long trid;

    public long getCounts() {
        return counts;
    }

    public void setCounts(long counts) {
        this.counts = counts;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTrid() {
        return trid;
    }

    public void setTrid(long trid) {
        this.trid = trid;
    }
}
