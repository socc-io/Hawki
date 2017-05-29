package com.socc.Hawki.app.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonParseException;
import com.socc.Hawki.app.R;
import com.socc.Hawki.app.deprecated.model.RecvData;
import com.socc.Hawki.app.service.HawkAPI;
import com.socc.Hawki.app.service.response.PostGetPositionRes;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeong on 2016-09-17.
 */
public class FinderActivity extends Activity {

    WifiManager wifimanager;
    List<ScanResult> wifiScanResult = new ArrayList<ScanResult>();
    List<RecvData> Indoor = new ArrayList<>();

    ImageView mapView;
    ListView listView;

    Bitmap mapViewBitmap;

    Paint mPaint;

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

        // Holding manager
        wifimanager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        // Holding View
        mapView = (ImageView) findViewById(R.id.mapView);
        listView = (ListView) findViewById(R.id.listView_building);

        // Holding bitmap ??? TODO: Holding mapViewBitmap
    }

    public void finderClicked(View v) throws JSONException {
        IntentFilter filter = new IntentFilter();

        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiReceiver, filter);
        wifimanager.startScan();
    }

    public void getWIFIScanResult() {
        wifiScanResult = wifimanager.getScanResults();

        try {
            HawkAPI api = HawkAPI.getInstance(); // get API Instance

            String bid = BuildingFragment.getInstance().getId(); // get BID

            PostGetPositionRes res = api.postGetPosition(bid, wifiScanResult); // do fetching
            if(res == null) {
                Toast.makeText(this, "실패했습니다", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.i("Get position response: ", res.toString());

            Bitmap newBitmap = mapViewBitmap.copy(Bitmap.Config.ARGB_8888, true);
            Canvas canvas = new Canvas(newBitmap);

            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(Color.RED);
            canvas.drawCircle(res.getX() * 32, res.getY() * 20, 30, mPaint);

            mapView.setImageBitmap(newBitmap);
            mapView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            unregisterReceiver(wifiReceiver);

        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



