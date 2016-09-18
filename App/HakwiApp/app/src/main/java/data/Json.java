package data;

import android.app.job.JobScheduler;
import android.net.wifi.ScanResult;

import com.example.jeong.httpclient.BuildingMarker;
import com.example.jeong.httpclient.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import data.DataSource.DATAFORMAT;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by joyeongje on 2016. 9. 4..
 */
public class Json {

    public static final int MAX_JSON_OBJECT = 50; // 최대 갯수

    public List<Marker> load(JSONObject root, DATAFORMAT dataformat) throws JSONException {
        JSONObject jo = null;
        JSONArray dataArray = null;
        List<Marker> markers = new ArrayList<Marker>();

        try {

            if(dataformat == DATAFORMAT.BuildingInfo)
                dataArray = root.getJSONArray("Build");

            else if(dataformat == DATAFORMAT.IndoorPosition)
                dataArray = root.getJSONArray("Indoor");

            else if(dataformat == DATAFORMAT.RSSIDSET)
                dataArray = root.getJSONArray("RssIdSet");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(dataArray != null) {

            Marker proMarker = null;
            int top = Math.min(MAX_JSON_OBJECT,dataArray.length());

            for(int i=0; i < top; i++) {
                jo = dataArray.getJSONObject(i);

                switch (dataformat) {
                    case BuildingInfo:
                        proMarker = processBuildingJsonObject(jo);
                        break;

                    case IndoorPosition:
                        proMarker = processIndoorJsonObject(jo);
                        break;

                    case RSSIDSET:
                        proMarker = processRSSIDSetJsonObject(jo);
                        break;
                }
                markers.add(proMarker);
            }

        }

        return markers;

    }

    public Marker processBuildingJsonObject(JSONObject jo) throws JSONException {
        Marker ma = null;

        if(jo.has("id") && jo.has("phone") && jo.has("title") && jo.has("address")) {

            ma = new BuildingMarker(jo.getString("id"),jo.getString("title"),null,null,
                                    0,0,jo.getString("phone"),jo.getString("address"));

            // TODO: 2016. 9. 4. has 에 들어갈거 정확히 서버랑 맞춰야된다.
            // 받아올것 건물 id, 경도, 위도, 주소 대표번호 건물명, 도면 url
            //ma = new BuildingMarker();

        }

        return ma;

    }

    public Marker processIndoorJsonObject(JSONObject jo) throws JSONException { // 실내위치

        Marker ma = null;
        return ma;

    }


    public Marker processRSSIDSetJsonObject(JSONObject jo) throws JSONException {
        Marker ma = null;
        return ma;
    }


    public JSONObject createRssiJson(String bid, String x, String y, String z, List<ScanResult> scanResults) throws JSONException {
        JSONObject collecterData = new JSONObject();
        collecterData.put("bid",bid);
        collecterData.put("x",x);
        collecterData.put("y",y);
        collecterData.put("z",z);

        JSONArray rssiArray = new JSONArray();

        for(int i = 0 ; i<scanResults.size() ; i++) {
            JSONObject rssidata = new JSONObject();
            rssidata.put("ssid",scanResults.get(i).SSID);
            rssidata.put("dbm",scanResults.get(i).level);
            rssiArray.put(i,rssidata);
        }
        collecterData.put("rssi",rssiArray);

        return collecterData;
    }




}
