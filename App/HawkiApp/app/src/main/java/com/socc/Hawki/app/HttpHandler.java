package com.socc.Hawki.app;

import android.app.ProgressDialog;
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

import DataPacket.Json;

/**
 * Created by Jeong on 2016-09-04.
 */
public class HttpHandler extends AsyncTask<String, Void, String> {
    private static final String TAG = HttpHandler.class.getSimpleName();

    @Override
    protected String doInBackground(String... params) { //params[0] = url, params[1] = get,post params[2] = json
        URL url;
        String response = null;
        String urlString = params[0];
        String httpFlag = params[1];

        if (httpFlag.equals("GET")) {

            try {
                url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-type", "application/json; charset=utf-8");
                conn.setRequestMethod("GET");

                int HttpResult = conn.getResponseCode();

                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    response = convertStreamToString(in);
                }

            } catch (MalformedURLException e) {
                Log.e(TAG, "MalformedURLException: " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e.getMessage());
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }

        } else if (httpFlag.equals("POST")) {

            try {
                url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-type", "application/json; charset=utf-8");
                conn.setRequestMethod("POST");

                OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream());
                os.write(params[2]);
                os.flush();

                int HttpResult = conn.getResponseCode();

                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    response = convertStreamToString(in);
                }

            } catch (MalformedURLException e) {
                Log.e(TAG, "MalformedURLException: " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e.getMessage());
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }

        }

        return response;
    }


    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
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

}


