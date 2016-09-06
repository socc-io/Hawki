package com.example.jeong.httpclient;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends ActionBarActivity {
    View rootView;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootView = (View)findViewById(android.R.id.content);
        editText = (EditText)findViewById(R.id.editText);
    }

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

        HttpHandler httpHandler = new HttpHandler(rootView);
        httpHandler.execute(url, "GET");
    }


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
