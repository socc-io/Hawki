package com.socc.Hawki.app.service.request;

import android.net.wifi.ScanResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gim-yeongjin on 2017. 5. 29..
 */

public class PostCollectRssiReq implements HawkRequest {
    String bid;
    float x;
    float y;
    float z;
    List<ScanResult> rssi;

    public PostCollectRssiReq(String bid, float x, float y, float z, List<android.net.wifi.ScanResult> rssi) {
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

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public List<ScanResult> getRssi() {
        return rssi;
    }

    public void setRssi(List<ScanResult> rssi) {
        this.rssi = rssi;
    }

    public class ScanResult {
        String bssid;
        int dbm;

        public ScanResult(String bssid, int dbm) {
            this.bssid = bssid;
            this.dbm = dbm;
        }

        public String getBssid() {
            return bssid;
        }

        public void setBssid(String bssid) {
            this.bssid = bssid;
        }

        public int getDbm() {
            return dbm;
        }

        public void setDbm(int dbm) {
            this.dbm = dbm;
        }
    }
}
