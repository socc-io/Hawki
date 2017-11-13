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
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jeong on 2016-09-16.
 */
public class CollectorActivity extends AppCompatActivity {

    private EditText editTextX, editTextY, editTextZ;
    private ImageView mapView;


    private WifiManager wifimanager;

    private TextView buildIdTextView;
    private TextView buildNameTextView;

    private Bitmap mapViewBitmap;

    private ImageView canvasView;
    private Bitmap canvasViewBitmap;

    private int mapImageWidth = 0;
    private int mapImageHeight = 0;

    private int mapImageContainerWidth = 0;
    private int mapImageContainerHeight = 0;

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
        } catch (IllegalArgumentException e) {

        } catch (Exception e) {
            Log.e("리시버가 아직 등록안됨.", e.getMessage());
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collector);

        // Holding view
        editTextX = (EditText)  findViewById(R.id.editTextX);
        editTextY = (EditText)  findViewById(R.id.editTextY);
        editTextZ = (EditText)  findViewById(R.id.editTextZ);

        //buildIdTextView = (TextView) findViewById(R.id.editText_buildingId);
        buildNameTextView = (TextView) findViewById(R.id.textView_buildingName);

        //buildIdTextView.setText(SingleTonBuildingInfo.getInstance().getSelectedBuildId());
        buildNameTextView.setText(SingleTonBuildingInfo.getInstance().getSelectedBuildName());
        // Holding WIFI manager
        wifimanager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        initMap();
        initCanvas();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //Here you can get the size!
        Log.d("onWindowFocusChanged","onWindowFocusChanged 호출");

        mapImageContainerWidth = canvasView.getWidth();
        mapImageContainerHeight = canvasView.getHeight();

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

                        editTextX.setText(String.valueOf((int) event.getX() / 80));
                        editTextY.setText(String.valueOf((int) event.getY() / 80));
                        editTextZ.setText(String.valueOf(0));
                }

                return true;
            }
        });


        // TODO: 2017. 11. 12 여기서 클릭이벤트 등록 기기

    }

    private void initMap() {

        Log.d("initMap","initMap 호출");
        mapView   = (ImageView) findViewById(R.id.mapView);
        String bid = SingleTonBuildingInfo.getInstance().getSelectedBuildId();
        String mapURL =  HawkiApplication.getMapImageURL(bid);
        Log.d("Map Url : ", mapURL);

        Picasso.with(getApplicationContext()).load(mapURL);
        Picasso.with(getApplicationContext()).load(mapURL).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                Log.d("onBitmapLoaded","onBitmapLoaded 호출");
                mapView.setImageBitmap(bitmap);
                mapView.setVisibility(View.VISIBLE);
                mapViewBitmap = bitmap;

                initCanvas();
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

        Log.d("canvasView","canvasView 호출");
        canvasView = (ImageView) findViewById(R.id.canvasView);
    }

    public void getWIFIScanResult() {
        String bid = SingleTonBuildingInfo.getInstance().getSelectedBuildId();
        float x = Float.parseFloat(editTextX.getText().toString());
        float y = Float.parseFloat(editTextY.getText().toString());
        float z = Float.parseFloat(editTextZ.getText().toString());
        List<ScanResult> wifiScanResult = wifimanager.getScanResults();

        PostCollectRssiReq req = new PostCollectRssiReq(bid, x, y, z, wifiScanResult);

//            HawkAPI api = HawkAPI.getInstance(); // get API Instance
//            String res = api.postCollectRssi(bid, x, y, z, wifiScanResult); // do fetching
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