package com.example.jeong.httpclient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MainActivity extends ActionBarActivity {
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.textView);



    }

    public void onButtonClicked(View v){
        String url = "http://192.168.0.2:4000/test";

        HttpUtil httpUtil = new HttpUtil();
        httpUtil.execute(url);
    }

    public JSONObject makeJson(){
        JSONObject jsonObj = new JSONObject();
        try {
//            jsonObj.put("name", "suno");
//            jsonObj.put("phone", "123-4567");

            jsonObj.put("bid", "0228777");
            jsonObj.put("name", "socc_building");
            jsonObj.put("longitude", "123");
            jsonObj.put("latitude", "456");

        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        return jsonObj;
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

    public class HttpUtil extends AsyncTask <String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            DefaultHttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);
            HttpParams httpParams = client.getParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
            HttpConnectionParams.setSoTimeout(httpParams, 3000);
            post.setHeader("Content-type", "application/json; charset=utf-8");

            try{
                StringEntity se=null;
                JSONObject jsonObject = makeJson();
                se = new StringEntity(jsonObject.toString());
                HttpEntity he=se;
                post.setEntity(he);

                HttpResponse response = client.execute(post);
                BufferedReader bufReader =
                        new BufferedReader(new InputStreamReader(
                                response.getEntity().getContent(), "utf-8")
                        );

                String line = null;
                String result = "";

                while ((line = bufReader.readLine())!=null){
                    result +=line;
                }
                return result;
            }
            catch(Exception e){
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String str) {
            //jsonParsing(str)
            textView.setText(str);
        }

    }

}
