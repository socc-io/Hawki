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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.socc.Hawki.app.R;
import com.socc.Hawki.app.application.HawkiApplication;
import com.socc.Hawki.app.service.SingleTonBuildingInfo;
import com.socc.Hawki.app.service.network.HttpService;
import com.socc.Hawki.app.service.request.PostCollectRssiReq;
import com.socc.Hawki.app.service.response.GetPoiListReq;
import com.socc.Hawki.app.service.response.Poi;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jeong on 2016-09-16.
 */
public class CollectorActivity extends AppCompatActivity {

    private WifiManager wifimanager;

    private Bitmap mapViewBitmap;
    private Bitmap canvasViewBitmap;

    private int mapImageWidth = 0;
    private int mapImageHeight = 0;

    private int mapImageContainerWidth = 0;
    private int mapImageContainerHeight = 0;

    private List<Poi> pois;
    private int xLoc, yLoc, zLoc = 0;

    @BindView(R.id.textView_buildingName)
    TextView buildNameTextView;

    @BindView(R.id.canvasView)
    ImageView canvasView;

    @BindView(R.id.mapView)
    ImageView mapView;

    private BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                getWIFIScanResult();
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(wifiReceiver);
        } catch (Exception e) {
            Log.e("리시버가 아직 등록안됨.", e.getMessage());
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collector);
        ButterKnife.bind(this);
        initMap();
        initMapViewTouchListener();
        initPOIDataList();

        buildNameTextView.setText(SingleTonBuildingInfo.getInstance().getSelectedBuildName());
        wifimanager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        Log.i("CollectorActivity", "thread start");
    }

    public void drawDot(int clickedX, int clickedY, int color) {
        Bitmap newDrawBitmap = canvasViewBitmap.copy(Bitmap.Config.ARGB_8888,true);
        Canvas canvas = new Canvas(newDrawBitmap);
        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(color);
        canvas.drawCircle(clickedX, clickedY, 10, mPaint);
        canvasView.setImageBitmap(newDrawBitmap);
    }

    public void drawDot(int clickedX, int clickedY) {
        drawDot(clickedX, clickedY, Color.RED);
    }

    private void initMap() {

        String bid = SingleTonBuildingInfo.getInstance().getSelectedBuildId();
        String mapURL = HawkiApplication.getMapImageURL(bid);
        Log.d("mapURL", mapURL);

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
                // TODO Auto-generated method stub
                mapViewBitmap = Bitmap.createBitmap(mapImageContainerWidth, mapImageContainerHeight, Bitmap.Config.ARGB_8888);
                mProgressDialog.dismiss();
            }
        };
        mapView.setTag(target);
        Picasso.with(this).load(mapURL).into(target);
    }

    private void initMapViewTouchListener() {

        canvasView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                canvasView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mapImageContainerWidth = canvasView.getMeasuredWidth();
                mapImageContainerHeight = canvasView.getMeasuredHeight();

                canvasViewBitmap = Bitmap.createBitmap(mapImageContainerWidth, mapImageContainerHeight, Bitmap.Config.ARGB_8888);
                canvasView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN: // react on only down event

                                Bitmap newDrawBitmap = canvasViewBitmap.copy(Bitmap.Config.ARGB_8888, true);

                                Log.d("mapImageContainerWidth", mapImageContainerWidth + "");
                                Log.d("mapImageContainerHeight", mapImageContainerHeight + "");
                                Log.d("mapImageWidth", mapImageWidth + "");
                                Log.d("mapImageHeight", mapImageHeight + "");
                                Log.d("event.getX()", event.getX() + "");
                                Log.d("event.getY()", event.getY() + "");

                                final int clickedX = (int) (event.getX());
                                final int clickedY = (int) (event.getY());

                                // Draw dots
                                Canvas canvas = new Canvas(newDrawBitmap);
                                Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                                mPaint.setStyle(Paint.Style.FILL);
                                mPaint.setColor(Color.RED);

                                canvas.drawCircle(clickedX, clickedY, 10, mPaint);

                                /*
                                 * 아래 코드는 POI의 파란점을 그리는 코드입니다. Collector에서는 그리지 않는 것으로 하고 주석처리되었습니다
                                 */
//                                mPaint.setColor(Color.BLUE);
//                                if(pois != null) {
//                                    for(Poi poi : pois) {
//                                        Integer x = (int)((float)poi.getX() / mapImageWidth * mapImageContainerWidth);
//                                        Integer y = (int)((float)poi.getY() / mapImageHeight * mapImageContainerHeight);
//                                        canvas.drawCircle(x, y, 10, mPaint);
//                                    }
//                                }

                                canvasView.setImageBitmap(newDrawBitmap);

                                int caculateX = (int) (event.getX() / mapImageContainerWidth * mapImageWidth);
                                int caculateY = (int) (event.getX() / mapImageContainerHeight * mapImageHeight);

                                Log.d("caculateX", caculateX + "");
                                Log.d("caculateY", caculateY + "");
                                Toast.makeText(getApplicationContext(), caculateX + " " + caculateY, Toast.LENGTH_SHORT).show();

                                xLoc = caculateX;
                                yLoc = caculateY;
                                zLoc = 0;
                        }

                        return true;
                    }
                });
            }
        });

    }

    public void getWIFIScanResult() {
        String bid = SingleTonBuildingInfo.getInstance().getSelectedBuildId();
        List<ScanResult> wifiScanResult = wifimanager.getScanResults();

        PostCollectRssiReq req = new PostCollectRssiReq(bid, xLoc, yLoc, zLoc, wifiScanResult);
        HttpService httpService = HawkiApplication.getRetrofit().create(HttpService.class);
        Call<JSONObject> call = httpService.postCollectRssi(req);

        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CollectorActivity.this,  "데이터 업로드 성공",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(CollectorActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initPOIDataList() {
        Log.d("initPOIDataList","initPOIDataList");
        HttpService httpService = HawkiApplication.getRetrofit().create(HttpService.class);
        Call<GetPoiListReq> poiListReqCall = httpService.getPOIList(SingleTonBuildingInfo.getInstance().getSelectedBuildId());
        poiListReqCall.enqueue(new Callback<GetPoiListReq>() {
            @Override
            public void onResponse(Call<GetPoiListReq> call, Response<GetPoiListReq> response) {
                if(response.isSuccessful()) {
                    pois = response.body().getPois();
                    Log.d("pois", pois.toString());
                }
            }

            @Override
            public void onFailure(Call<GetPoiListReq> call, Throwable t) {

            }
        });
    }

    public void collectorClicked(View v) throws JSONException {
        if (wifimanager == null)
            wifimanager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiReceiver, filter);
        wifimanager.startScan();
    }
}