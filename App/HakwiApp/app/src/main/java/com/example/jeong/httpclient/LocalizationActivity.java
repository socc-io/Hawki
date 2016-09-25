package com.example.jeong.httpclient;

import android.app.Activity;
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

import data.DataSource;
import data.Json;

/**
 * Created by Jeong on 2016-09-17.
 */
public class LocalizationActivity extends Activity{

    // WifiManager variable
    WifiManager wifimanager;
    List<ScanResult> wifiScanResult = new ArrayList<ScanResult>();
    List<Marker> Indoor = new ArrayList<>();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localization);


        // Setup WIFI
        wifimanager = (WifiManager) getSystemService(WIFI_SERVICE);
    }

    //버튼클릭 이벤트
    //TODO:버튼 클릭시 현재 실내위치 띄워주기
    public void localizationClicked(View v) throws JSONException {

        WifiCollector wifiCollector = new WifiCollector(wifimanager);
        wifiScanResult = wifiCollector.getWIFIScanResult();

        Json layer = new Json();
        HttpHandler httpHandler = new HttpHandler();
        // TODO: 2016. 9. 17. 컬렉트 액티비티 시나리오

        String locateUrl = DataSource.createRequestURL(DataSource.DATAFORMAT.IndoorPosition,0,0,0,0,null);
        JSONObject rssiJsonObject = layer.createRequestIndoorJson(BuildingFragment.getInstance().getBuildId(),wifiScanResult);

        // return x,y
        try {
            String result = httpHandler.execute(locateUrl,rssiJsonObject.toString()).get().toString();
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            JSONObject jsonObject = new JSONObject(result);
            Indoor = layer.load(jsonObject ,DataSource.DATAFORMAT.IndoorPosition);

            IndoorMarker indoorMarker = (IndoorMarker)Indoor.get(0);
            Toast.makeText(getApplicationContext(),indoorMarker.getX() + " " + indoorMarker.getY() + " " + indoorMarker.getZ() ,Toast.LENGTH_LONG).show();

            Log.i("리절트값",result.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("rssijson test",rssiJsonObject.toString());

    }
}
