package com.socc.Hawki.app.view;

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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonParseException;
import com.socc.Hawki.app.R;
import com.socc.Hawki.app.deprecated.model.RecvData;
import com.socc.Hawki.app.service.HawkAPI;
import com.socc.Hawki.app.service.SingleTonBuildingInfo;
import com.socc.Hawki.app.service.response.PostGetPositionRes;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeong on 2016-09-17.
 */
public class FinderActivity extends AppCompatActivity {

    WifiManager wifimanager;
    List<ScanResult> wifiScanResult = new ArrayList<ScanResult>();
    List<RecvData> Indoor = new ArrayList<>();

    ImageView mapView;
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

        TextView buildIdTextView = (TextView)findViewById(R.id.textView_buildingId);
        buildIdTextView.setText(SingleTonBuildingInfo.getInstance().getSelectedBuildId());
        TextView buildNameTextView = (TextView) findViewById(R.id.textView_buildName);
        buildNameTextView.setText(SingleTonBuildingInfo.getInstance().getSelectedBuildName());

        initMap();
    }

    private void initMap() {
        mapView = (ImageView) findViewById(R.id.mapView2);
        String buildId = SingleTonBuildingInfo.getInstance().getSelectedBuildId();
        String mapURL =  HawkAPI.getInstance().getMapImageURL(buildId);
        Log.d("Map Url : ", mapURL);
        Picasso.with(getApplicationContext()).load(mapURL).resize(400,400).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                mapView.setImageBitmap(bitmap);
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

            String bid = SingleTonBuildingInfo.getInstance().getSelectedBuildId();
            PostGetPositionRes res = api.postGetPosition(bid, wifiScanResult); // do fetching
            if(res == null) {
                Toast.makeText(this, "실패했습니다", Toast.LENGTH_SHORT).show();
                return;
            }

            Bitmap newDrawBitmap = mapViewBitmap.copy(Bitmap.Config.ARGB_8888, true);
            Canvas canvas = new Canvas(newDrawBitmap);
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(Color.RED);
            canvas.drawCircle(res.getX() * 20, res.getY() * 20, 5, mPaint);
            mapView.setImageBitmap(newDrawBitmap);

            unregisterReceiver(wifiReceiver);

        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



