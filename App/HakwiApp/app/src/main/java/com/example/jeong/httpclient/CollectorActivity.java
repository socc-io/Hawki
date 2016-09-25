package com.example.jeong.httpclient;

import android.app.Activity;
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
import java.util.concurrent.ExecutionException;

import data.DataSource;
import data.Json;

/**
 * Created by Jeong on 2016-09-16.
 */
public class CollectorActivity  extends Activity {

    final String TAG = CollectorActivity.class.getSimpleName();
    EditText editTextX, editTextY, editTextZ;

    // WifiManager variable
    WifiManager wifimanager;
    List<ScanResult> wifiScanResult = new ArrayList<ScanResult>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collector);

        editTextX = (EditText)findViewById(R.id.editTextX);
        editTextY = (EditText)findViewById(R.id.editTextY);
        editTextZ = (EditText)findViewById(R.id.editTextZ);

        // Setup WIFI
        wifimanager = (WifiManager) getSystemService(WIFI_SERVICE);
        //Log.d(TAG, "Setup WIfiManager getSystemService");

    }

    //버튼클릭 이벤트
    //TODO: 버튼 클릭시 현재위치 수집기능(서버에 rssi 보내주기) 구현
    //TODO: xyz입력시 키패드가 가림 현상 수정!
    public void collectorClicked(View v) throws JSONException {
        String loc_x = editTextX.getText().toString();
        String loc_y = editTextY.getText().toString();
        String loc_z = editTextZ.getText().toString();

        Toast.makeText(getApplication(), loc_x+","+loc_y+","+loc_z, Toast.LENGTH_LONG).show();

        WifiCollector wifiCollector = new WifiCollector(wifimanager);
        wifiScanResult = wifiCollector.getWIFIScanResult();

        Json layer = new Json();

        HttpHandler httpHandler = new HttpHandler();

        String collectorUrl = DataSource.createRequestURL(DataSource.DATAFORMAT.RSSIDSET,0,0,0,0,null);
        // TODO: 2016. 9. 19. 여기에 x,y,z 받아와서 넣어야됨
        JSONObject rssiJsonObject = layer.createRssiJson(BuildingFragment.getInstance().getBuildId(),loc_x,loc_y,loc_z,wifiScanResult);

        Log.i("url test",collectorUrl.toString());
        Log.i("rssijson test",rssiJsonObject.toString());

        try {
            String result = httpHandler.execute(collectorUrl,rssiJsonObject.toString()).get().toString();
            Log.i("리절트값",result.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        // TODO: 2016. 9. 17. 여기서 포스트로 보내는거 주는거 나랑 순호형거 합치고 그리고 서버코드도 통합.

    }

}