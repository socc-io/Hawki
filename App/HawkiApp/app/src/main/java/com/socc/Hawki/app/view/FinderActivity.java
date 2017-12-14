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
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonParseException;
import com.socc.Hawki.app.R;
import com.socc.Hawki.app.application.HawkiApplication;
import com.socc.Hawki.app.service.LocationPosition;
import com.socc.Hawki.app.service.OrientationSensor;
import com.socc.Hawki.app.service.SingleTonBuildingInfo;
import com.socc.Hawki.app.service.network.HttpService;
import com.socc.Hawki.app.service.request.PostGetPositionReq;
import com.socc.Hawki.app.service.response.GetPoiListReq;
import com.socc.Hawki.app.service.response.Poi;
import com.socc.Hawki.app.service.response.PostGetPositionRes;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
    List<LocationPosition> locationHistory = new ArrayList<LocationPosition>();
    List<OrientationSensor> sensorHistory = new ArrayList<OrientationSensor>();
    public float current_x = 0;
    public float current_y = 0;
    public float current_z = 0;

    int PDR_HISTORY_COUNT = 5;
    final int SENSOR_HISTORY_COUNT = 10;
    private final static double EPSILON = 0.00001;
    private final static long PDR_TASK_INTERVAL = 3000;

    private boolean isTrackButton = false;


    private void addLocationHistory(LocationPosition lp) {
        locationHistory.add(lp);
        if (locationHistory.size() > PDR_HISTORY_COUNT) {
            locationHistory.remove(0);
        }
    }

    private void addSensorHistory(SensorEvent sensor) {
        sensorHistory.add(new OrientationSensor(sensor.values[0], sensor.values[1], sensor.values[2]));
        if (sensorHistory.size() > SENSOR_HISTORY_COUNT) {
            sensorHistory.remove(0);
        }
    }

    private Bitmap canvasViewBitmap;
    private PostGetPositionRes res = null;
    private Timer pdrTimer;

    Paint mPaint;

    private int canvasWidth = 0;
    private int canvasHeight = 0;

    private float mapImageWidth = 0;
    private float mapImageHeight = 0;

    private List<Poi> pois;

    private SensorManager mSensorManager;
    private final float[] mAccelerometerReading = new float[3];
    private final float[] mMagnetometerReading = new float[3];

    private final float[] mRotationMatrix = new float[9];
    private final float[] mOrientationAngles = new float[3];

    private float initialAngle = 0.0f;
    private int index = 0;

    @BindView(R.id.mapView)
    ImageView mapview;

    @BindView(R.id.canvasView)
    ImageView canvasView;

    @BindView(R.id.ic_search)
    ImageButton searchButton;

    @BindView(R.id.ic_track)
    ImageButton trackButton;

    @BindView(R.id.poiTitle)
    TextView poiTitleTextView;

    @BindView(R.id.poiTag)
    TextView poiTagTextView;

    public void PDR_dot_update(int speed, boolean use_history) {
        if(current_x == 0 && current_y== 0  && current_z == 0) return;


        float tmp_x = current_x;
        float tmp_y = current_y;
        float tmp_z = current_z;
        float dir_x = 0;
        float dir_y = 0;
        float dir_z = 0;

        if(use_history) {
            for (int i = locationHistory.size() - 2; i >= 0; i--) {
                LocationPosition loc = locationHistory.get(i);
                dir_x += tmp_x - loc.x;
                dir_y += tmp_y - loc.y;
                dir_z += tmp_z - loc.z;
                tmp_x = loc.x;
                tmp_y = loc.y;
                tmp_z = loc.z;
            }
            double vec_len = Math.sqrt(dir_x * dir_x + dir_y * dir_y + dir_z * dir_z) + EPSILON;
            dir_x /= vec_len;
            dir_y /= vec_len;
            dir_z /= vec_len;
        }else{
            updateOrientationAngles();
            float newAngle = mOrientationAngles[0];
            float turn_angle = 0.0f;
            if(Math.signum(initialAngle) == Math.signum(newAngle)){
                turn_angle = newAngle - initialAngle;
            }else{
                turn_angle = newAngle - initialAngle + Math.signum(initialAngle) * 2 * (float) Math.PI;
            }
            dir_x = (float)Math.sin(turn_angle);
            dir_y = -1.0f * (float)Math.cos(turn_angle);
            dir_z = 0.0f;
        }
        Log.d("PDR_dot_vector", "dir_x : " + dir_x + ", dir_y : " + dir_y + ", dir_z : " + dir_z);
        current_x += dir_x * speed;
        current_y += dir_y * speed;
        current_z += dir_z * speed;

        drawDot(current_x, current_y, dir_x, dir_y );
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

        initMap();
        initCanvasView();
        initPOIDataList();
        initMapViewTouchListener();

        if (wifimanager == null)
            wifimanager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiReceiver, filter);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        trackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isTrackButton = !isTrackButton;
                if(isTrackButton) {
                    pdrTimer = new Timer();
                    pdrTimer.schedule(new PDRTask(), PDR_TASK_INTERVAL, PDR_TASK_INTERVAL);
                    Log.i("--PDR scheduler--", "pdr swtich on --> task is schduled!");
                } else {
                    pdrTimer.cancel();
                    Log.i("--PDR scheduler--", "pdr swtich off --> task cancel!");
                }
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017-12-14 activity change
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
    }

    private void initPOIDataList() {

        HttpService httpService = HawkiApplication.getRetrofit().create(HttpService.class);
        Call<GetPoiListReq> poiListReqCall = httpService.getPOIList(SingleTonBuildingInfo.getInstance().getSelectedBuildId());
        poiListReqCall.enqueue(new Callback<GetPoiListReq>() {
            @Override
            public void onResponse(Call<GetPoiListReq> call, Response<GetPoiListReq> response) {
                if (response.isSuccessful()) {
                    pois = response.body().getPois();
                    Log.d("pois", pois.toString());
                }
            }

            @Override
            public void onFailure(Call<GetPoiListReq> call, Throwable t) {

            }
        });
    }

    private void initMapViewTouchListener() {


        canvasView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                canvasView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                canvasWidth  = canvasView.getMeasuredWidth();
                canvasHeight = canvasView.getMeasuredHeight();

                canvasViewBitmap = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888);
                canvasView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN: // react on only down event

                                Log.d("mapImageContainerWidth", canvasWidth + "" );
                                Log.d("mapImageContainerHeight", canvasHeight + "" );
                                Log.d("mapImageWidth", mapImageWidth + "" );
                                Log.d("mapImageHeight",mapImageHeight + "");
                                Log.d("event.getX()",event.getX() + "");
                                Log.d("event.getY()",event.getY() + "" );

                                final int clickedX = (int) (event.getX());
                                final int clickedY = (int) (event.getY());

                                int calculateX =  (int)(event.getX() / canvasWidth * mapImageWidth);
                                int calculateY =  (int)(event.getY() / canvasHeight * mapImageHeight);

                                drawDot(calculateX, calculateY, 0, -1);

                                Log.d("caculateX", calculateX + "" );
                                Log.d("caculateY",calculateY + "");
                                Toast.makeText(getApplicationContext(), calculateX + " " + calculateY,Toast.LENGTH_SHORT).show();

                                current_x = calculateX;
                                current_y = calculateY;
                                current_z = 0.0f;
                                updateOrientationAngles();
                                initialAngle = mOrientationAngles[0];

                                if(pois != null) {
                                    Poi nearest_poi = null;
                                    double min_dist = 9999999.0f;
                                    for(Poi poi: pois) {
                                        double dx = poi.getX() - calculateX;
                                        double dy = poi.getY() - calculateY;
                                        double dist = Math.sqrt(dx * dx + dy * dy);
                                        if(dist < min_dist) {
                                            min_dist = dist;
                                            nearest_poi = poi;
                                        }
                                    }
                                    Toast.makeText(FinderActivity.this, nearest_poi.getName(), Toast.LENGTH_SHORT).show();
                                }
                        }

                        return true;
                    }
                });
            }
        });
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

        String buildId = "21160803";
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
                mapview.setImageBitmap(arg0);
                mapImageWidth = arg0.getWidth();
                mapImageHeight = arg0.getHeight();
                mProgressDialog.dismiss();

                // Draw dots
                Bitmap newDrawBitmap = canvasViewBitmap.copy(Bitmap.Config.ARGB_8888,true);
                Canvas canvas = new Canvas(newDrawBitmap);
                Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(Color.BLUE);
                if(pois != null) {
                    for(Poi poi : pois) {
                        Integer x = (int)((float)poi.getX() / mapImageWidth * canvasWidth);
                        Integer y = (int)((float)poi.getY() / mapImageHeight * canvasHeight);
                        canvas.drawCircle(x, y, 10, mPaint);
                    }
                }
                canvasView.setImageBitmap(newDrawBitmap);
            }

            @Override
            public void onBitmapFailed(Drawable arg0) {
                mProgressDialog.dismiss();
            }
        };
        mapview.setTag(target);
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

    public void drawDot(float cliecdX, float cliecdY, float dirX, float dirY) {
        float calculateX = cliecdX / mapImageWidth * canvasWidth;
        float calculateY = cliecdY / mapImageHeight * canvasHeight;
        Bitmap newDrawBitmap = canvasViewBitmap.copy(Bitmap.Config.ARGB_8888, true);

        Canvas canvas = new Canvas(newDrawBitmap);
        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.RED);
        canvas.drawCircle(calculateX, calculateY, 20, mPaint);

        Paint mPaint_dir = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_dir.setStyle(Paint.Style.FILL);
        mPaint_dir.setColor(Color.RED);
        canvas.drawCircle(calculateX + dirX * 25, calculateY + dirY * 25, 7, mPaint);

        mPaint.setColor(Color.BLUE);
        if(pois != null) {
            for(Poi poi : pois) {
                Integer x = (int)((float)poi.getX() / mapImageWidth * canvasWidth);
                Integer y = (int)((float)poi.getY() / mapImageHeight * canvasHeight);
                canvas.drawCircle(x, y, 10, mPaint);
            }
        }

        canvasView.setImageBitmap(newDrawBitmap);
    }

    public void getWIFIScanResult() {
        wifiScanResult = wifimanager.getScanResults();

        try {
            //String bid = SingleTonBuildingInfo.getInstance().getSelectedBuildId();
            String bid = "21160803"; //강남역
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
                        if(res != null) {
                            current_x = res.getX();
                            current_y = res.getY();
                            current_z = 0;
                            addLocationHistory(new LocationPosition(current_x, current_y, current_z));
                            drawDot(current_x, current_y, 0, -1);
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
        //wifimanager.startScan();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)) {
            System.arraycopy(event.values, 0, mAccelerometerReading,
                    0, mAccelerometerReading.length);
            if (checkMoveStatus(event)) {
                Log.i("Accelerometer", "--moving--" + event.values[0] + "," + event.values[1] + "," + event.values[2]);
                //PDR_dot_update(5, false);
            }
        } else if (event.sensor == mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)) {
            System.arraycopy(event.values, 0, mMagnetometerReading,
                    0, mMagnetometerReading.length);
            PDR_dot_update(1, false);
        }
        updateOrientationAngles();
        //Log.i("Orientation", "--orientation ( " + mOrientationAngles[0] + "," + mOrientationAngles[1] + "," + mOrientationAngles[2] + ")");
    }

    public void updateOrientationAngles() {
        mSensorManager.getRotationMatrix(mRotationMatrix, null,
                mAccelerometerReading, mMagnetometerReading);
        mSensorManager.getOrientation(mRotationMatrix, mOrientationAngles);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // Do something here if sensor accuracy changes.
        // You must implement this callback in your code.
    }

    public boolean checkMoveStatus(SensorEvent event) {
        if (sensorHistory.size() == 10) {
            float sumX = 0;
            float sumY = 0;
            float sumZ = 0;
            for (int i = 0; i < sensorHistory.size(); i++) {
                sumX += sensorHistory.get(i).getX();
                sumY += sensorHistory.get(i).getY();
                sumZ += sensorHistory.get(i).getZ();
            }

            addSensorHistory(event);

            double avgX = sumX / 10.0;
            double avgY = sumY / 10.0;
            double avgZ = sumZ / 10.0;

            //Log.i("checkMoveStatus", avgX + "," + event.values[0] + "///" + avgY + "," + event.values[1] + avgZ + "," + event.values[2]);
            if ((Math.abs(avgX - event.values[0]) + Math.abs(avgY - event.values[1]) + Math.abs(avgZ - event.values[2])) > 10) {
                return true; //working
            } else {
                return false; //don't working
            }
        } else {
            addSensorHistory(event);
            return false; //don't working
        }
    }

    private class PDRTask extends TimerTask {
        @Override
        public void run() {
            wifimanager.startScan();
            Log.i("--PDR Task--", "pdr task is running!!");
        }
    }
}

