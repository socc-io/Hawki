package com.socc.Hawki.app.service.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.socc.Hawki.app.service.ScanResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gim-yeongjin on 2017. 5. 29..
 */

public class PostGetPositionReq {

    @SerializedName("bid")
    @Expose
    private String bid;

    @SerializedName("data")
    private
    List<ScanResult> rssi;

    public PostGetPositionReq(String bid, List<android.net.wifi.ScanResult> rssi) {
        this.bid = bid;
        this.rssi = new ArrayList<>();
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

    public List<ScanResult> getRssi() {
        return rssi;
    }

    public void setRssi(List<ScanResult> rssi) {
        this.rssi = rssi;
    }

}
