package com.socc.Hawki.app.service.request;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gim-yeongjin on 2017. 5. 29..
 */

public class PostGetPositionReq implements HawkRequest {
    String bid;
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
