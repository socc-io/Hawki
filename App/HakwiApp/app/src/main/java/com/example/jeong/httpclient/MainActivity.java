package com.example.jeong.httpclient;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    public static final int REQUEST_CODE_COLLECTOR = 1001;
    public static final int REQUEST_CODE_LOCALIZATION = 1004;
    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 9001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);
        }

    }

    public void collectorBtnClicked(View v){
        Intent intent = new Intent(getApplicationContext(), CollectorActivity.class);
        startActivityForResult(intent, REQUEST_CODE_COLLECTOR);
    }
    public void finderBtnClicked(View v){
        Intent intent = new Intent(getApplicationContext(), FinderActivity.class);
        startActivityForResult(intent, REQUEST_CODE_LOCALIZATION);
    }

    //응답처리, 필요할경우 사용하기위해 미리 짜놓음!
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if(requestCode == REQUEST_CODE_COLLECTOR){
            if(resultCode == RESULT_OK){
                String name = intent.getExtras().getString("res");
                Toast.makeText(getBaseContext(), "응답:" + name, Toast.LENGTH_LONG).show();
            }
        }
        else if(requestCode == REQUEST_CODE_LOCALIZATION){
            if(resultCode == RESULT_OK){
                String name = intent.getExtras().getString("res");
                Toast.makeText(getBaseContext(), "응답:" + name, Toast.LENGTH_LONG).show();
            }
        }
    }


    /*
    public void onPostBtnClicked(View v){
        String url = "http://192.168.0.32:4000/test";
        HttpHandler httpHandler = new HttpHandler(rootView);
        httpHandler.execute(url, "POST");
    }

    public void onGetBtnClicked(View v){
        String url = editText.getText().toString();
        if(!url.contains("http://")){
            url = "http://" + url;
        }

        // (DATAFORMAT dataformat, double lat,double lon, double alt, double radius, String name)
        HttpHandler httpHandler = new HttpHandler(rootView);
        httpHandler.execute(url, "GET");
    }*/

//    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener()
//    {
//        public void onItemClick(AdapterView<?> adapterView, View clickedView, int pos, long id)
//        {
//            selectedBuildId = markers.get(pos).getBuildId();
//            String toastMessage = selectedBuildId;
//            Toast.makeText(
//                    getApplicationContext(),
//                    toastMessage,
//                    Toast.LENGTH_SHORT
//            ).show();
//        }
//    };




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}