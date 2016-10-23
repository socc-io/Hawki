package com.socc.Hawki.app;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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

    ImageView mapView;
    ListView listView;

    Paint mPaint;

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
                final int getX = Integer.parseInt(indoorMarker.getX());
                final int getY = Integer.parseInt(indoorMarker.getY());

                String selectedBuildId = BuildingFragment.getInstance().getBuildId();

                String mapImageUrl = "http://beaver.hp100.net:4000/static/map/" +  selectedBuildId +".jpg";
                Log.d("Map Url : ", mapImageUrl);

                Picasso.with(getApplicationContext()).load(mapImageUrl).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Bitmap newBitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true);
                        Canvas canvas = new Canvas(newBitmap);

                        float x_Size = newBitmap.getWidth() / 20;
                        float y_Size = newBitmap.getHeight() / 10;

                        Log.i("x_size",Float.toString(x_Size));
                        Log.i("y_size", Float.toString(y_Size));

                        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                        mPaint.setStyle(Paint.Style.STROKE);
                        mPaint.setStrokeWidth(3);
                        mPaint.setColor(Color.RED);
                        canvas.drawCircle(x_Size * getX,y_Size * getY,15,mPaint);

                        mapView.setImageBitmap(newBitmap);

                        mapView.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);

                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        // TODO: 2016. 10. 23. 실패했을시 이미지 넣기.

                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.noimage);
                        Bitmap newBitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true);

                        Canvas canvas = new Canvas(newBitmap);

                        float x_Size = newBitmap.getWidth() / 20;
                        float y_Size = newBitmap.getHeight() / 10;

                        Log.i("x_size",Float.toString(x_Size));
                        Log.i("y_size", Float.toString(y_Size));

                        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                        mPaint.setStyle(Paint.Style.STROKE);
                        mPaint.setStrokeWidth(3);
                        mPaint.setColor(Color.RED);
                        canvas.drawCircle(x_Size * getX,y_Size * getY,15,mPaint);

                        mapView.setImageBitmap(newBitmap);

                        mapView.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });

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

        mapView = (ImageView) findViewById(R.id.mapView);
        listView = (ListView) findViewById(R.id.list);
    }


    public void finderClicked(View v) throws JSONException {

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiReceiver, filter);
        wifimanager.startScan();

    }
}