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
import android.graphics.drawable.Drawable;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.socc.Hawki.app.R;
import com.socc.Hawki.app.model.RecvData;
import com.socc.Hawki.app.util.HttpHandler;
import com.socc.Hawki.app.model.IndoorData;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.socc.Hawki.app.util.URLMaker;
import com.socc.Hawki.app.DataPacket.Json;

/**
 * Created by Jeong on 2016-09-17.
 */
public class FinderActivity extends Activity {

    WifiManager wifimanager;
    List<ScanResult> wifiScanResult = new ArrayList<ScanResult>();
    List<RecvData> Indoor = new ArrayList<>();

    ImageView mapView;
    ListView listView;

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
        wifimanager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        mapView = (ImageView) findViewById(R.id.mapView);
        listView = (ListView) findViewById(R.id.listView_building);
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
            Json layer = new Json();

            String locateUrl = URLMaker.createRequestURL(URLMaker.DATAFORMAT.IndoorPosition, 0, 0, 0, 0, null);
            JSONObject rssiJsonObject = layer.createRequestIndoorJson(BuildingFragment.getInstance().getBuildId(), wifiScanResult);
            String result = new HttpHandler().execute(locateUrl, "POST", rssiJsonObject.toString()).get();

            if (result != null) {
                String convertStr = Json.convertStandardJSONString(result);
                JSONObject jsonObject = new JSONObject(convertStr);
                Indoor = layer.load(jsonObject, URLMaker.DATAFORMAT.IndoorPosition);

                IndoorData indoorMarker = (IndoorData) Indoor.get(0);
                final int getX = Integer.parseInt(indoorMarker.getX());
                final int getY = Integer.parseInt(indoorMarker.getY());

              //  Bitmap newBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
              //  Canvas canvas = new Canvas(newBitmap);
//
              //  mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
              //  mPaint.setStyle(Paint.Style.FILL);
              //  mPaint.setColor(Color.RED);
              //  canvas.drawCircle(getX * 32, getY * 20, 30, mPaint);
//
              //  mapView.setImageBitmap(newBitmap);
              //  mapView.setVisibility(View.VISIBLE);
              //  listView.setVisibility(View.GONE);
              //  unregisterReceiver(wifiReceiver);

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}



