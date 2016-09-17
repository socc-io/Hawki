package com.example.jeong.httpclient;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Jeong on 2016-09-17.
 */
public class LocalizationActivity extends Activity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localization);

    }

    //버튼클릭 이벤트
    //TODO:버튼 클릭시 현재 실내위치 띄워주기
    public void localizationClicked(View v){
        //MainActivity로 응답 보낼 때 사용
        /*Intent intent = new Intent();
        intent.putExtra("res","보낼응답!");

        setResult(RESULT_OK, intent);
        finish();*/


    }
}
