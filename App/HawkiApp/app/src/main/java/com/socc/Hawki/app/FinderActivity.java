package com.socc.Hawki.app;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import DataPacket.DataSource;
import DataPacket.Json;

/**
 * Created by Jeong on 2016-09-17.
 */
public class FinderActivity extends Activity {

    WifiManager wifimanager;
    List<ScanResult> wifiScanResult = new ArrayList<ScanResult>();
    List<Data> Indoor = new ArrayList<>();

    public void getWIFIScanResult() {
        wifiScanResult = wifimanager.getScanResults();

        try {
            Json layer = new Json();

            String locateUrl = DataSource.createRequestURL(DataSource.DATAFORMAT.IndoorPosition, 0, 0, 0, 0, null);
            JSONObject rssiJsonObject = layer.createRequestIndoorJson(BuildingFragment.getInstance().getBuildId(), wifiScanResult);
            String result = new HttpHandler().execute(locateUrl,"POST",rssiJsonObject.toString()).get();

            if(result != null) {
                String convertStr = Json.convertStandardJSONString(result);
                JSONObject jsonObject = new JSONObject(convertStr);
                Indoor = layer.load(jsonObject, DataSource.DATAFORMAT.IndoorPosition);
                IndoorData indoorMarker = (IndoorData) Indoor.get(0);
                int getX = Integer.parseInt(indoorMarker.getX());
                int getY = Integer.parseInt(indoorMarker.getY());

                Toast.makeText(getApplicationContext(),"x : " + getX + ", y : " + getY ,Toast.LENGTH_LONG).show();
            }
            else
                Toast.makeText(getApplicationContext(),"아직 서버에 학습되지 않은 상태입니다",Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }

        unregisterReceiver(wifiReceiver);

    }

    private BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                getWIFIScanResult();
            }
        }
    };


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finder);
        wifimanager = (WifiManager) getSystemService(WIFI_SERVICE);
    }


    public void finderClicked(View v) throws JSONException {

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiReceiver, filter);
        wifimanager.startScan();

    }
}