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
import android.net.ParseException;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.socc.Hawki.app.R;
import com.socc.Hawki.app.service.HawkAPI;
import com.socc.Hawki.app.util.HttpHandler;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.socc.Hawki.app.util.URLMaker;
import com.socc.Hawki.app.DataPacket.Json;

/**
 * Created by Jeong on 2016-09-16.
 */
public class CollectorActivity extends Activity {

    EditText editTextX, editTextY, editTextZ;
    ImageView mapView;
    ListView listView;

    WifiManager wifimanager;
    List<ScanResult> wifiScanResult = new ArrayList<ScanResult>();

    Paint mPaint;

    String loc_x;
    String loc_y;
    String loc_z;

    private Bitmap mapViewBitmap;

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

        // Holding view
        editTextX = (EditText)  findViewById(R.id.editTextX);
        editTextY = (EditText)  findViewById(R.id.editTextY);
        editTextZ = (EditText)  findViewById(R.id.editTextZ);
        mapView   = (ImageView) findViewById(R.id.mapView);
        listView  = (ListView)  findViewById(R.id.listView_building);

        // Holding WIFI manager
        wifimanager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        // Attach touch listener to MapView
        mapView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // react on only down event

                        /*
                        클릭된 좌표를 인식해서 editText의 X, Y, Z 좌표를 자동으로 수정해준다
                         */

                        final float x = event.getX();
                        final float y = event.getY();

                        Bitmap newDrawBitmap = mapViewBitmap.copy(Bitmap.Config.ARGB_8888, true);
                        Canvas canvas = new Canvas(newDrawBitmap);

                        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                        mPaint.setStyle(Paint.Style.FILL);
                        mPaint.setColor(Color.RED);
                        canvas.drawCircle(x, y, 30, mPaint);

                        mapView.setImageBitmap(newDrawBitmap);

                        mapView.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);

                        editTextX.setText(String.valueOf((int)x/32));
                        editTextY.setText(String.valueOf((int)y/20));
                        editTextZ.setText(String.valueOf(0));

                }
                return true;
            }
        });
    }


    public void getWIFIScanResult() {
        wifiScanResult = wifimanager.getScanResults();

        loc_x = editTextX.getText().toString();
        loc_y = editTextY.getText().toString();
        loc_z = editTextZ.getText().toString();

        Toast.makeText(getApplication(), loc_x + " ," + loc_y + " ," + loc_z, Toast.LENGTH_LONG).show();

        try {
            String bid = BuildingFragment.getInstance().getBuildId(); // get BID

            float x = Float.parseFloat(loc_x); // parse as Float
            float y = Float.parseFloat(loc_y);
            float z = Float.parseFloat(loc_z);

            HawkAPI api = HawkAPI.getInstance(); // get API Instance
            JsonObject res = api.postCollectRssi(bid, x, y, z, wifiScanResult); // do fetching

            if(res == null) {
                Toast.makeText(this, "실패했습니다", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.i("Collect response: ", res.toString());

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        unregisterReceiver(wifiReceiver);
    }

    public void collectorClicked(View v) throws JSONException {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiReceiver, filter);
        wifimanager.startScan();
    }


}