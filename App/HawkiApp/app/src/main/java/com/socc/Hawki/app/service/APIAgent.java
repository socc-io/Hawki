package com.socc.Hawki.app.service;

import android.util.Pair;

import com.socc.Hawki.app.model.BuildingData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gim-yeongjin on 2017. 5. 15..
 */

public class APIAgent {
    protected Map<String, String> headers;
    protected String baseURL;

    public APIAgent(String baseURL) {
        headers = new HashMap<>();
        this.baseURL = baseURL;
    }

    public void setHeader(String key, String value) {
        headers.put(key, value);
    }
    public void setHeaders(List<Pair<String, String>> list) {
        for(Pair<String, String> o : list) {
            headers.put(o.first, o.second);
        }
    }
    public void removeHeader(String key) {
        headers.remove(key);
    }

    public String getHttpResponse(String urlAppend, String method, String body) {
        try {
            URL url = new URL(baseURL + urlAppend);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod(method);

            if(body != null) {
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(URLEncoder.encode(body, "UTF-8"));
                writer.close();
            }

            for(String key : headers.keySet()) { // set headers
                conn.setRequestProperty(key, headers.get(key));
            }

            int respCode = conn.getResponseCode();
            if(respCode == HttpURLConnection.HTTP_OK || respCode == HttpURLConnection.HTTP_NOT_FOUND) {
                ArrayList<BuildingData> resultData = new ArrayList<>();
                StringBuffer response = new StringBuffer();
                String line;

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                return response.toString();
            }
            return null;
        }
        catch(MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        catch(IOException e) {
            e.printStackTrace();
            return null;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
