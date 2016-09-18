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
        editTextX.setText("");
        editTextY.setText("");
        editTextY.setText("");
        Toast.makeText(getApplication(), loc_x+","+loc_y+","+loc_z, Toast.LENGTH_LONG).show();

        WifiCollector wifiCollector = new WifiCollector(wifimanager);
        wifiScanResult = wifiCollector.getWIFIScanResult();

        Json layer = new Json();

        HttpHandler httpHandler = new HttpHandler();
        try {
            String result = httpHandler.execute("url","json").get().toString();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
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