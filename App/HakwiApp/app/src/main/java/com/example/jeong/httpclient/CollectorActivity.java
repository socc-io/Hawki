package com.example.jeong.httpclient;

import android.app.Activity;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import data.DataSource;
import data.Json;

/**
 * Created by Jeong on 2016-09-16.
 */
public class CollectorActivity  extends Activity {

    final String TAG = CollectorActivity.class.getSimpleName();
    EditText editText;

    // WifiManager variable
    WifiManager wifimanager;
    List<ScanResult> wifiScanResult = new ArrayList<ScanResult>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collector);

        editText = (EditText)findViewById(R.id.editText_location);

        // Setup WIFI
        wifimanager = (WifiManager) getSystemService(WIFI_SERVICE);
        //Log.d(TAG, "Setup WIfiManager getSystemService");

    }

    //버튼클릭 이벤트
    //TODO: 버튼 클릭시 현재위치 수집기능(서버에 rssi 보내주기) 구현
<<<<<<< HEAD
    public void collectorClicked(View v){
=======
<<<<<<< HEAD
    public void collectorClicked(View v) throws JSONException {
=======
    public void localizationClicked(View v){
>>>>>>> 13d2f8ea2f2d2f0edd064d3d8e6286c65f113831
        String loc = editText.getText().toString();
        Toast.makeText(getApplication(), loc, Toast.LENGTH_LONG).show();
>>>>>>> afe8b8751f2862569861cc9f4bdd526569331095

        WifiCollector wifiCollector = new WifiCollector(wifimanager);
        wifiScanResult = wifiCollector.getWIFIScanResult();

        Json layer = new Json();

        HttpHandler httpHandler = new HttpHandler();
        // TODO: 2016. 9. 17. 컬렉트 액티비티 시나리오
        String collectorUrl = DataSource.createRequestURL(DataSource.DATAFORMAT.RSSIDSET,0,0,0,0,null);
        JSONObject rssiJsonObject = layer.createRssiJson("bid","x","y","z",wifiScanResult);
        Log.i("rssijson test",rssiJsonObject.toString());
        // 2. Datasource.json 에 있는 함수를 이용해서 wifiscanResultstring 을 json형태로 담음
        // 3. 통합된 서버 http post 코드로 new GetContacts().execute(makeURL, "POST")방식으로 함
        // 4. 서버에 정상적으로 올려져있는지 확인하면 이기능 구현 끝
        httpHandler.makeRssiSetJson(wifiScanResult.toString());
        Log.i("collector to string",wifiScanResult.toString());
        // TODO: 2016. 9. 17. 여기서 포스트로 보내는거 주는거 나랑 순호형거 합치고 그리고 서버코드도 통합.

    }

}