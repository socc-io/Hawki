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
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import data.DataSource;
import data.Json;

/**
 * Created by Jeong on 2016-09-16.
 */
public class CollectorActivity extends Activity {

    final String TAG = CollectorActivity.class.getSimpleName();
    EditText editTextX, editTextY, editTextZ;

    // WifiManager variable
    WifiManager wifimanager;
    List<ScanResult> wifiScanResult = new ArrayList<ScanResult>();

    public void getWIFIScanResult() {
        wifiScanResult = wifimanager.getScanResults(); // ScanResult

        String loc_x = editTextX.getText().toString();
        String loc_y = editTextY.getText().toString();
        String loc_z = editTextZ.getText().toString();

        Toast.makeText(getApplication(), loc_x+","+loc_y+","+loc_z, Toast.LENGTH_LONG).show();

        try {
            Json layer = new Json();
            HttpHandler httpHandler = new HttpHandler();

            String collectorUrl = DataSource.createRequestURL(DataSource.DATAFORMAT.RSSIDSET, 0, 0, 0, 0, null);
            JSONObject rssiJsonObject = layer.createRssiJson(BuildingFragment.getInstance().getBuildId(), loc_x, loc_y, loc_z, wifiScanResult);

            Log.i("url test", collectorUrl);
            Log.i("rssijson test", rssiJsonObject.toString());

            String result = httpHandler.execute(collectorUrl, rssiJsonObject.toString()).get();

            Log.i("result : ", result.toString());

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collector);

        editTextX = (EditText) findViewById(R.id.editTextX);
        editTextY = (EditText) findViewById(R.id.editTextY);
        editTextZ = (EditText) findViewById(R.id.editTextZ);

        // Setup WIFI
        wifimanager = (WifiManager) getSystemService(WIFI_SERVICE);
        //Log.d(TAG, "Setup WIfiManager getSystemService");

    }

    public void collectorClicked(View v) throws JSONException {

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiReceiver, filter);

        wifimanager.startScan();


    }

}