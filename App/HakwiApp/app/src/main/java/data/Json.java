package data;

import android.app.job.JobScheduler;

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




}
