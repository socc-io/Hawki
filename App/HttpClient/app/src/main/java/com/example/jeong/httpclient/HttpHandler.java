package com.example.jeong.httpclient;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by Jeong on 2016-09-04.
 */
public class HttpHandler extends AsyncTask<String, Void, String> {
    TextView textView;
    View rootView; //MainActivity

    public HttpHandler(View rootView){
        this.rootView = rootView;
    }
    @Override
    protected String doInBackground(String... params) {
        String url = params[0];
        String method = params[1];
        HttpResponse response = null;

        DefaultHttpClient client = new DefaultHttpClient();

        if(method.equals("POST")){
            HttpPost post = new HttpPost(url);
            HttpParams http_params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(http_params, 3000);
            HttpConnectionParams.setSoTimeout(http_params, 3000);
            post.setHeader("Content-type", "application/json; charset=utf-8");

            JSONObject jsonObject = makeJson();
            StringEntity str_entity = null;
            try {
                str_entity = new StringEntity(jsonObject.toString());

                HttpEntity http_entity = str_entity;
                post.setEntity(http_entity);
                response = client.execute(post);//post
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if(method.equals("GET")){
            HttpGet get = new HttpGet(url);
            try {
                response = client.execute(get);//post
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //read data
        BufferedReader bufReader = null;
        String line = null;
        String result = "";

        try {
            bufReader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent(), "utf-8")
            );

            while ((line = bufReader.readLine()) != null) {
                result += line;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }


        return result;
    }

    @Override
    protected void onPostExecute(String res_str) {

        textView = (TextView) rootView.findViewById(R.id.textView);
        textView.setText(res_str);
        Log.d("---onPostExecute---: ", res_str);
    }

    public JSONObject makeJson(){
        JSONObject jsonObj = new JSONObject();
        try {

            jsonObj.put("bid", "0228777");
            jsonObj.put("name", "socc_building");
            jsonObj.put("longitude", "123");
            jsonObj.put("latitude", "456");

        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        return jsonObj;
    }

}
