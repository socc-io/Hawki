package com.socc.Hawki.app.service.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.socc.Hawki.app.service.ScanResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gim-yeongjin on 2017. 5. 29..
 */

public class PostCollectRssiReq {

    @SerializedName("bid")
    @Expose
    private String bid;

    @SerializedName("data")
    @Expose
    private List<ScanResult> rssi = null;

    @SerializedName("x")
    @Expose
    private int x;

    @SerializedName("y")
    @Expose
    private int y;

    @SerializedName("z")
    @Expose
    private int z;

    public PostCollectRssiReq(String bid, int x, int y, int z, List<android.net.wifi.ScanResult> rssi) {
        this.bid = bid;
        this.x = x;
        this.y = y;
        this.z = z;
        this.rssi = new ArrayList<ScanResult>();
        for(android.net.wifi.ScanResult scan: rssi) {
            this.rssi.add(new ScanResult(scan.BSSID, scan.level));
        }
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public List<ScanResult> getRssi() {
        return rssi;
    }

    public void setRssi(List<ScanResult> rssi) {
        this.rssi = rssi;
    }


}
