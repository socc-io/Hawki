package com.example.jeong.httpclient;

import java.util.List;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.pm.PackageManager.*;

/**
 * Created by joyeongje on 2016. 9. 11..
 */


public class WifiCollector {

    private static final String TAG = "WIFIScanner";

    // WifiManager variable
    WifiManager wifimanager;

    WifiCollector(WifiManager wifimanager) {
        this.wifimanager = wifimanager;
        initWIFIScan();
    }
    private List<ScanResult> mScanResult; // ScanResult List

    public List<ScanResult> getWIFIScanResult() {

        mScanResult = wifimanager.getScanResults(); // ScanResult
        //ssid 랑 레벨 가져와야함
        // ssid 는 투스트링 레벨은 dbm
        return mScanResult;

    }

    public void initWIFIScan() {
        // if WIFIEnabled
        if (wifimanager.isWifiEnabled() == false)
            wifimanager.setWifiEnabled(true);

        wifimanager.startScan();

    }

}
