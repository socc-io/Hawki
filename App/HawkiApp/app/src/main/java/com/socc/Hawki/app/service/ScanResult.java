package com.socc.Hawki.app.service;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by joyeongje on 2017. 11. 25..
 */

public class ScanResult {

        @SerializedName("bssid")
        @Expose
        String bssid;

        @SerializedName("dbm")
        @Expose
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
