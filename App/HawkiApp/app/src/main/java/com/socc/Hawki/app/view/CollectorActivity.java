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

import com.socc.Hawki.app.R;
import com.socc.Hawki.app.service.HawkAPI;
import com.socc.Hawki.app.service.SingleTonMapview;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

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

    String bid;

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

        bid = "17573702";
        initMap(bid);

        // Attach touch listener to MapView
        mapView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // react on only down event

                        final int cliecdX = (int) (event.getX() / 4);
                        final int cliecdY = (int) (event.getY() / 4);

                        Log.d("찍은 좌표 위치값",cliecdX + " " + cliecdY);

                        Bitmap newDrawBitmap = mapViewBitmap.copy(Bitmap.Config.ARGB_8888,true);
                        Canvas canvas = new Canvas(newDrawBitmap);
                        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                        mPaint.setStyle(Paint.Style.FILL);
                        mPaint.setColor(Color.RED);
                        canvas.drawCircle(cliecdX,cliecdY,5,mPaint);

                        mapView.setImageBitmap(newDrawBitmap);

                        editTextX.setText(String.valueOf((int)event.getX() / 80));
                        editTextY.setText(String.valueOf((int)event.getY() / 80));
                        editTextZ.setText(String.valueOf(0));

                }
                return true;
                // TODO: 2017-06-02 코드 정리. 
            }
        });
    }

    private void initMap(String bid) {
        String mapURL =  HawkAPI.getInstance().getMapImageURL(bid);
        Log.d("Map Url : ", mapURL);
        Picasso.with(getApplicationContext()).load(mapURL).resize(400,400).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                mapView.setImageBitmap(bitmap);
                Log.d("맵뷰크기" , mapView.getWidth() + " " + mapView.getHeight());
                mapView.setVisibility(View.VISIBLE);
                mapViewBitmap = bitmap;
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

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
            //String bid = BuildingFragment.getInstance().getId(); // get BID

            bid = "17573702";

            float x = Float.parseFloat(loc_x); // parse as Float
            float y = Float.parseFloat(loc_y);
            float z = Float.parseFloat(loc_z);

            HawkAPI api = HawkAPI.getInstance(); // get API Instance
            String res = api.postCollectRssi(bid, x, y, z, wifiScanResult); // do fetching

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