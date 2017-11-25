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
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
import com.socc.Hawki.app.service.SingleTonBuildingInfo;
import com.socc.Hawki.app.service.network.HttpService;
import com.socc.Hawki.app.service.request.PostGetPositionReq;
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
public class FinderActivity extends AppCompatActivity implements SensorEventListener {

    WifiManager wifimanager;
    List<ScanResult> wifiScanResult = new ArrayList<ScanResult>();

    private Bitmap canvasViewBitmap;
    private PostGetPositionRes res = null;

    Paint mPaint;

    private int canvasWidth = 0;
    private int canvasHeight = 0;

    private int mapImageWidth = 0;
    private int mapImageHeight = 0;

    private SensorManager mSensorManager;
    private final float[] mAccelerometerReading = new float[3];
    private final float[] mMagnetometerReading = new float[3];

    private final float[] mRotationMatrix = new float[9];
    private final float[] mOrientationAngles = new float[3];

    private
    private int index = 0;

    @BindView(R.id.mapView2)
    ImageView mapView;

    @BindView(R.id.canvasView2)
    ImageView canvasView;

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

        if (wifimanager == null)
            wifimanager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiReceiver, filter);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(wifiReceiver);
            mSensorManager.unregisterListener(this);
        } catch (Exception e) {
            Log.e("Don't Regist Receiver.", e.getMessage());
        }

    }

    private void initMap() {
        String buildId = SingleTonBuildingInfo.getInstance().getSelectedBuildId();
        String mapURL = HawkiApplication.getMapImageURL(buildId);
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
                canvasViewBitmap = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888);

                Log.d("canvasWidth", canvasWidth + "");
                Log.d("canvasHeight", canvasHeight + "");

                Log.d("mapImageWidth", mapImageWidth + "");
                Log.d("mapImageHeight", mapImageHeight + "");


            }
        });
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

                    if (response.isSuccessful()) {
                        res = response.body();
                        if (res != null) {
                            Bitmap newDrawBitmap = canvasViewBitmap.copy(Bitmap.Config.ARGB_8888, true);
                            Canvas canvas = new Canvas(newDrawBitmap);
                            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                            mPaint.setStyle(Paint.Style.FILL);
                            mPaint.setColor(Color.RED);

                            int xLoc = (int) res.getX();
                            int yLoc = (int) res.getY();
                            int zLoc = 0;

                            Log.d("caculateX", xLoc + "");
                            Log.d("caculateY", yLoc + "");

                            canvas.drawCircle(xLoc, yLoc, 10, mPaint);
                            canvasView.setImageBitmap(newDrawBitmap);
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


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)) {
            System.arraycopy(event.values, 0, mAccelerometerReading,
                    0, mAccelerometerReading.length);

            checkMoveStatus(event);

            Log.i("onSensorChanged", event.values[0] + "," + event.values[1] + "," + event.values[2]);
        } else if (event.sensor == mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)) {
            System.arraycopy(event.values, 0, mMagnetometerReading,
                    0, mMagnetometerReading.length);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // Do something here if sensor accuracy changes.
        // You must implement this callback in your code.
    }

    public boolean checkMoveStatus(SensorEvent event){
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        double result = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
        if(result > 5.0){
            Log.i("checkMoveStatus", "--moving--");

            return true;
        }
        else{
            Log.i("checkMoveStatus", "--don't moving--");

            return false;
        }
    }
}



