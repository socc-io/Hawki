package com.socc.Hawki.app;

import android.app.Activity;
import android.app.Fragment;
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
import android.media.Image;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

import static com.socc.Hawki.app.R.id.mapView;

/**
 * Created by Jeong on 2016-09-16.
 */
public class CollectorActivity extends Activity {

    final String TAG = CollectorActivity.class.getSimpleName();
    EditText editTextX, editTextY, editTextZ;
    ImageView mapView;
    ListView listView;

    WifiManager wifimanager;
    List<ScanResult> wifiScanResult = new ArrayList<ScanResult>();

    Paint mPaint;

    public void getWIFIScanResult() {
        wifiScanResult = wifimanager.getScanResults(); // ScanResult

        final String loc_x = editTextX.getText().toString();
        final String loc_y = editTextY.getText().toString();
        final String loc_z = editTextZ.getText().toString();

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
                canvas.drawCircle(x_Size * Float.parseFloat(loc_x),y_Size * Float.parseFloat(loc_y),15,mPaint);

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
                canvas.drawCircle(x_Size * Integer.parseInt(loc_x),y_Size * Integer.parseInt(loc_y),15,mPaint);

                mapView.setImageBitmap(newBitmap);

                mapView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

        Toast.makeText(getApplication(), loc_x + " ," + loc_y + " ," + loc_z, Toast.LENGTH_LONG).show();

        try {

            Json layer = new Json();
            String collectorUrl = DataSource.createRequestURL(DataSource.DATAFORMAT.RSSIDSET, 0, 0, 0, 0, null);
            JSONObject rssiJsonObject = layer.createRssiJson(BuildingFragment.getInstance().getBuildId(), loc_x, loc_y, loc_z, wifiScanResult);
            Log.i("Wifi Json : ", rssiJsonObject.toString());

            String result = new HttpHandler().execute(collectorUrl,"POST",rssiJsonObject.toString()).get();
            Log.i("Collect Result", result);

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

        mapView = (ImageView) findViewById(R.id.mapView);
        listView = (ListView) findViewById(R.id.list);

        wifimanager = (WifiManager) getSystemService(WIFI_SERVICE);

    }

    public void collectorClicked(View v) throws JSONException {

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiReceiver, filter);
        wifimanager.startScan();

    }

    public void onDraw() {


    }







}