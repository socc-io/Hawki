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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;

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

    private int xLoc,yLoc,zLoc = 0;

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

        buildNameTextView.setText(SingleTonBuildingInfo.getInstance().getSelectedBuildName());
        wifimanager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
    }

    private void initMap() {

        String bid = SingleTonBuildingInfo.getInstance().getSelectedBuildId();
        String mapURL = HawkiApplication.getMapImageURL(bid);
        Log.d("mapURL",mapURL);


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
                mapViewBitmap = arg0;
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

                                mapImageWidth = mapViewBitmap.getWidth();
                                mapImageHeight = mapViewBitmap.getHeight();
                                Bitmap newDrawBitmap = canvasViewBitmap.copy(Bitmap.Config.ARGB_8888,true);

                                Log.d("mapImageWidth", mapImageWidth + "" );
                                Log.d("mapImageHeight",mapImageHeight + "");
                                Log.d("event.getX()",event.getX() + "");
                                Log.d("event.getY()",event.getY() + "" );
                                final int cliecdX = (int) (event.getX());
                                final int cliecdY = (int) (event.getY());

                                Canvas canvas = new Canvas(newDrawBitmap);
                                Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                                mPaint.setStyle(Paint.Style.FILL);
                                mPaint.setColor(Color.RED);
                                canvas.drawCircle(cliecdX, cliecdY, 10, mPaint);
                                canvasView.setImageBitmap(newDrawBitmap);


                                int caculateX = (int) (((float)mapImageContainerWidth / (float)mapImageWidth) *  event.getX());
                                int caculateY = (int) (((float)mapImageContainerHeight / (float)mapImageHeight) *  event.getX());

                                Log.d("caculateX", caculateX + "" );
                                Log.d("caculateY",caculateY + "");

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
        Call<String> call = httpService.postCollectRssi(req);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.body() == null) {
                    Toast.makeText(CollectorActivity.this, "실패했습니다", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(CollectorActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void collectorClicked(View v) throws JSONException {
        if(wifimanager == null)
            wifimanager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiReceiver, filter);
        wifimanager.startScan();
    }

}