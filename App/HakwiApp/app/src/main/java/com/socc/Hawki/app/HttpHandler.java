package com.socc.Hawki.app;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Jeong on 2016-09-04.
 */
public class HttpHandler extends AsyncTask<String, Void, String>{
    private static final String TAG = HttpHandler.class.getSimpleName();

    @Override
    protected String doInBackground(String... params) {//params[0] = url, params[1] = json
        URL url;
        String response = null;
        StringBuilder sb = new StringBuilder();
        try {

            url = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestProperty("Content-type", "application/json; charset=utf-8");
            conn.setRequestMethod("POST");
            conn.setDoInput(true);//응답 헤더와 메시지를 읽어들이겠다
            conn.setDoOutput(true);

            //write json
            OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream());
            os.write(params[1]);

            //os.wrote(json.toString());
            // TODO: 2016. 9. 17. makeRssiSetJson 바뀜에 따라 일단 주석
            os.flush();
            InputStream in = new BufferedInputStream(conn.getInputStream());

            //read response
            int HttpResult = conn.getResponseCode();

            if (HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                String line = null;

                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
            } else {
                Log.d("TAG", "HttpResult error!");
            }

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        }

        return sb.toString();
    }

    public String httpGet(String reqUrl){
        String response = null;

        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }

        return response;
    }


    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        try {
            String line;
            while ((line = reader.readLine()) != null) {

                sb.append(line + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        sb = sb.deleteCharAt(0);

        return sb.toString();
    }

    public JSONObject makeRssiSetJson(String rssiSetJson) {

        // TODO: 2016. 9. 17. 여기서 제이썬 처리를 해줘야될듯
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


