package com.example.jeong.httpclient;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Jeong on 2016-09-16.
 */
public class CollectorActivity  extends Activity {
    final String TAG = CollectorActivity.class.getSimpleName();
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collector);
        editText = (EditText)findViewById(R.id.editText_location);

    }

    //버튼클릭 이벤트
    //TODO: 버튼 클릭시 현재위치 수집기능(서버에 rssi 보내주기) 구현
    public void collectorClicked(View v){
        String loc = editText.getText().toString();
        Toast.makeText(getApplication(), loc, Toast.LENGTH_LONG).show();

    }

}