package com.socc.Hawki.app.model;

/**
 * Created by joyeongje on 2016. 9. 4..
 */
public class IndoorData extends RecvData {

    private String x,y,z; // 내 위치

    public IndoorData(String bid, String title, String description, String url, String x, String y,
                      String z) {
        super(bid,title,description,url);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getZ() {
        return z;
    }

    public void setZ(String z) {
        this.z = z;
    }
}
