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


public class WifiCollector extends Activity implements OnClickListener {

    private static final String TAG = "WIFIScanner";

    // WifiManager variable
    WifiManager wifimanager;

    // UI variable
    TextView textStatus;
    Button btnScanStart;
    Button btnScanStop;

    private int scanCount = 0;
    String text = "";

    private List<ScanResult> mScanResult; // ScanResult List


    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                getWIFIScanResult(); // get WIFISCanResult
                wifimanager.startScan(); // for refresh
                Log.d(TAG, "BroadcastReceiver()");
            } else if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                sendBroadcast(new Intent("wifi.ON_NETWORK_STATE_CHANGED"));
                Log.d(TAG, "BroadcastReceiver()");
            }
        }
    };

    public void getWIFIScanResult() {

        Log.d(TAG, "getWIFIScanResult()");
        mScanResult = wifimanager.getScanResults(); // ScanResult
        // Scan count
        textStatus.setText("Scan count is \t" + ++scanCount + " times \n");

        textStatus.append("=======================================\n");

        for (int i = 0; i < mScanResult.size(); i++) {
            //ScanResult result = mScanResult.get(i);
            textStatus.append((i + 1) + ". SSID : " + mScanResult.get(i).SSID.toString() + "\t\t RSSI : " + mScanResult.get(i).level + " dBm\n");
        }
        textStatus.append("=======================================\n");
    }

    public void initWIFIScan() {
        // init WIFISCAN
        scanCount = 0;
        text = "";
        final IntentFilter filter = new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(mReceiver, filter);
        wifimanager.startScan();
        Log.d(TAG, "initWIFIScan()");
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_collector);

        // Setup UI
        textStatus = (TextView) findViewById(R.id.textStatus);
        btnScanStart = (Button) findViewById(R.id.btnScanStart);
        btnScanStop = (Button) findViewById(R.id.btnScanStop);

        // Setup OnClickListener
        btnScanStart.setOnClickListener(this);
        btnScanStop.setOnClickListener(this);

        // Setup WIFI
        wifimanager = (WifiManager) getSystemService(WIFI_SERVICE);
        Log.d(TAG, "Setup WIfiManager getSystemService");

        // if WIFIEnabled
        if (wifimanager.isWifiEnabled() == false)
            wifimanager.setWifiEnabled(true);

    }

    public void printToast(String messageToast) {
        Toast.makeText(this, messageToast, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnScanStart) {
            Log.d(TAG, "OnClick() btnScanStart()");
            printToast("WIFI COLLECT !!!");
            initWIFIScan(); // start WIFIScan
        }
        if (v.getId() == R.id.btnScanStop) {
            Log.d(TAG, "OnClick() btnScanStop()");
            printToast("Collect STOP !!!");
            unregisterReceiver(mReceiver); // stop WIFISCan
        }
    }
}
