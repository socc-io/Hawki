package com.example.jeong.httpclient;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Jeong on 2016-09-16.
 */
public class CollectorActivity  extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collector);

    }

    //버튼클릭 이벤트
    //TODO: 버튼 클릭시 현재위치 수집기능(서버에 rssi 보내주기) 구현
    public void collectorClicked(View v){

//        Intent intent = new Intent();
//        intent.putExtra("res","보낼응답!");
//
//        setResult(RESULT_OK, intent);
//        finish();

    }

}