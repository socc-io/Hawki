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
import com.socc.Hawki.app.application.HawkiApplication;
import com.socc.Hawki.app.service.SingleTonBuildingInfo;
import com.socc.Hawki.app.service.network.HttpService;
import com.socc.Hawki.app.service.request.PostGetPositionReq;
import com.socc.Hawki.app.service.response.PostGetPositionRes;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jeong on 2016-09-17.
 */
public class FinderActivity extends AppCompatActivity {

    WifiManager wifimanager;
    List<ScanResult> wifiScanResult = new ArrayList<ScanResult>();

    private ImageView canvasView;
    private Bitmap canvasViewBitmap;
    private PostGetPositionRes res = null;

    Paint mPaint;

    ImageView mapView;
    TextView tvFinderX;
    TextView tvFinderY;
    TextView tvFinderZ;

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

        TextView buildNameTextView = (TextView) findViewById(R.id.textView_buildName);
        buildNameTextView.setText(SingleTonBuildingInfo.getInstance().getSelectedBuildName());

        tvFinderX = (TextView)findViewById(R.id.textView_finder_x);
        tvFinderY = (TextView)findViewById(R.id.textView_finder_y);
        tvFinderZ = (TextView)findViewById(R.id.textView_finder_z);

        initMap();
        initCanvas();

        if(wifimanager == null)
            wifimanager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(wifiReceiver);
        } catch (IllegalArgumentException e) {

        } catch (Exception e) {
            Log.e("Don't Regist Receiver.", e.getMessage());
        }

    }

    private void initMap() {
        mapView = (ImageView) findViewById(R.id.mapView2);
        String buildId = SingleTonBuildingInfo.getInstance().getSelectedBuildId();
        String mapURL =  HawkiApplication.getMapImageURL(buildId);
        Log.d("Map Url : ", mapURL);
        Picasso.with(getApplicationContext()).load(mapURL).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                mapView.setImageBitmap(bitmap);
                mapView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }


    private void initCanvas() {
        canvasView = (ImageView) findViewById(R.id.canvasView2);
        canvasViewBitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
    }

    public void getWIFIScanResult() {
        wifiScanResult = wifimanager.getScanResults();

        try {
            String bid = SingleTonBuildingInfo.getInstance().getSelectedBuildId();
            PostGetPositionReq req = new PostGetPositionReq(bid, wifiScanResult);

            HttpService httpService = HawkiApplication.getRetrofit().create(HttpService.class);
            Call<PostGetPositionRes> call = httpService.postGetPosition(req);

            call.enqueue(new Callback<PostGetPositionRes>() {
                @Override
                public void onResponse(Call<PostGetPositionRes> call, Response<PostGetPositionRes> response) {
                    if(response.body() == null) {
                        Toast.makeText(FinderActivity.this, "실패했습니다", Toast.LENGTH_SHORT).show();
                    }

                    res = response.body();
                    Bitmap newDrawBitmap = canvasViewBitmap.copy(Bitmap.Config.ARGB_8888, true);
                    Canvas canvas = new Canvas(newDrawBitmap);
                    mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    mPaint.setStyle(Paint.Style.FILL);
                    mPaint.setColor(Color.RED);

                    tvFinderX.setText(res.getX()+"");
                    tvFinderY.setText(res.getY()+"");
                    tvFinderZ.setText(res.getZ()+"");

                    canvas.drawCircle(res.getX() * 20, res.getY() * 20, 5, mPaint);
                    canvasView.setImageBitmap(newDrawBitmap);
                }

                @Override
                public void onFailure(Call<PostGetPositionRes> call, Throwable t) {
                    t.printStackTrace();
                }
            });

        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void finderClicked(View v) throws JSONException {

        wifimanager.startScan();
    }
}



