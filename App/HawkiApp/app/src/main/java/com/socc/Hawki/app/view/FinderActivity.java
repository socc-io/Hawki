package com.socc.Hawki.app.view;

import android.app.ProgressDialog;
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
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonParseException;
import com.socc.Hawki.app.R;
import com.socc.Hawki.app.application.HawkiApplication;
import com.socc.Hawki.app.service.LocationPosition;
import com.socc.Hawki.app.service.SingleTonBuildingInfo;
import com.socc.Hawki.app.service.network.HttpService;
import com.socc.Hawki.app.service.request.PostGetPositionReq;
import com.socc.Hawki.app.service.response.GetPoiListReq;
import com.socc.Hawki.app.service.response.Poi;
import com.socc.Hawki.app.service.response.PostGetPositionRes;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jeong on 2016-09-17.
 */
public class FinderActivity extends AppCompatActivity {

    WifiManager wifimanager;
    List<ScanResult> wifiScanResult = new ArrayList<ScanResult>();
    List<LocationPosition> locationHistory = new ArrayList<LocationPosition>();
    public float current_x = 0;
    public float current_y = 0;
    public float current_z = 0;

    int PDR_HISTORY_COUNT = 5;
    private final static double EPSILON = 0.00001;


    private void addLocationHistory(LocationPosition lp) {
        locationHistory.add(lp);
        if (locationHistory.size() > PDR_HISTORY_COUNT){
            locationHistory.remove(0);
        }
    }

    private Bitmap canvasViewBitmap;
    private PostGetPositionRes res = null;

    Paint mPaint;

    private int canvasWidth = 0;
    private int canvasHeight = 0;

    private float mapImageWidth = 0;
    private float mapImageHeight = 0;

    @BindView(R.id.mapView2)
    ImageView mapView;

    @BindView(R.id.canvasView2)
    ImageView canvasView;

    public void PDR_dot_update(int speed){
        float tmp_x = current_x; float tmp_y = current_y; float tmp_z = current_z;
        float dir_x= 0; float dir_y = 0; float dir_z = 0;
        for(int i = locationHistory.size()-1; i >=0; i--){
            LocationPosition loc = locationHistory.get(i);
            dir_x += tmp_x - loc.x; dir_y += tmp_y - loc.y; dir_z += tmp_z - loc.z;
            tmp_x = loc.x; tmp_y = loc.y; tmp_z = loc.z;
        }
        double vec_len = Math.sqrt(dir_x * dir_x + dir_y * dir_y + dir_z * dir_z) + EPSILON;
        dir_x /= vec_len; dir_y /= vec_len; dir_z /= vec_len;
        current_x += dir_x * speed; current_y += dir_y * speed; current_z += dir_z * speed;
        drawDot(current_x, current_y);
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

        ButterKnife.bind(this);

        TextView buildNameTextView = (TextView) findViewById(R.id.textView_buildName);
        buildNameTextView.setText(SingleTonBuildingInfo.getInstance().getSelectedBuildName());

        initMap();
        initCanvasView();
        initPOIDataList();

        if(wifimanager == null)
            wifimanager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiReceiver, filter);
    }

    private void initPOIDataList() {

        HttpService httpService = HawkiApplication.getRetrofit().create(HttpService.class);
        Call<GetPoiListReq> poiListReqCall = httpService.getPOIList(SingleTonBuildingInfo.getInstance().getSelectedBuildId());
        poiListReqCall.enqueue(new Callback<GetPoiListReq>() {
            @Override
            public void onResponse(Call<GetPoiListReq> call, Response<GetPoiListReq> response) {
                if(response.isSuccessful()) {
                    List<Poi> pois = response.body().getPois();
                    Log.d("pois", pois.toString());
                }
            }

            @Override
            public void onFailure(Call<GetPoiListReq> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(wifiReceiver);
        } catch (Exception e) {
            Log.e("Don't Regist Receiver.", e.getMessage());
        }
    }

    private void initMap() {
        String buildId = SingleTonBuildingInfo.getInstance().getSelectedBuildId();
        String mapURL =  HawkiApplication.getMapImageURL(buildId);
        Log.d("Map Url : ", mapURL);

        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("지도를 불러오는중...");
        mProgressDialog.setIndeterminate(false);

        Target target = new Target() {
            @Override
            public void onPrepareLoad(Drawable arg0) {
                mProgressDialog.show();
            }

            @Override
            public void onBitmapLoaded(Bitmap arg0, Picasso.LoadedFrom arg1) {
                mapView.setImageBitmap(arg0);
                mapImageWidth = arg0.getWidth();
                mapImageHeight = arg0.getHeight();
                mProgressDialog.dismiss();
            }

            @Override
            public void onBitmapFailed(Drawable arg0) {
//                canvasViewBitmap = Bitmap.createBitmap(canvasWidth,canvasHeight,Bitmap.Config.ARGB_8888);
          //      canvasViewBitmap = Bitmap.createBitmap(mapView.getMeasuredWidth(),mapView.getMeasuredHeight(),Bitmap.Config.ARGB_8888);
//                mapViewBitmap = Bitmap.createBitmap(mapImageContainerWidth, mapImageContainerHeight, Bitmap.Config.ARGB_8888);
                mProgressDialog.dismiss();
            }
        };
        mapView.setTag(target);
        Picasso.with(this).load(mapURL).into(target);
    }

    private void initCanvasView() {
        canvasView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                canvasWidth = canvasView.getMeasuredWidth();
                canvasHeight = canvasView.getMeasuredHeight();
                canvasViewBitmap = Bitmap.createBitmap(canvasWidth,canvasHeight,Bitmap.Config.ARGB_8888);
                Log.d("canvasWidth", canvasWidth + "");
                Log.d("canvasHeight", canvasHeight + "");

                Log.d("mapImageWidth", mapImageWidth + "");
                Log.d("mapImageHeight", mapImageHeight + "");
            }
        });
    }

    public void drawDot(float cliecdX, float cliecdY){
        Bitmap newDrawBitmap = canvasViewBitmap.copy(Bitmap.Config.ARGB_8888,true);
        Canvas canvas = new Canvas(newDrawBitmap);
        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.RED);
        canvas.drawCircle(cliecdX, cliecdY, 10, mPaint);
        canvasView.setImageBitmap(newDrawBitmap);
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
                    Log.d("rescanvasWidth", canvasWidth + "");
                    Log.d("rescanvasHeight", canvasHeight + "");

                    Log.d("resmapImageWidth", mapImageWidth + "");
                    Log.d("resmapImageHeight", mapImageHeight + "");

                    if(response.isSuccessful()) {
                        res = response.body();
                        if(res != null) {
                            current_x  =  res.getX();
                            current_y =  res.getY();
                            current_z = 0;
                            addLocationHistory(new LocationPosition(current_x, current_y, current_z));

                            float caculateX = current_x / mapImageWidth * canvasWidth ;
                            float caculateY = current_y / mapImageHeight * canvasHeight;
                            //TODO : xLoc, yLoc should be change to display resolution
                            drawDot(caculateX,caculateY);

                            Log.d("caculateX", caculateX + "");
                            Log.d("caculateY", caculateY + "");
                        }

                    }
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



