package DataPacket;

import android.net.wifi.ScanResult;

import com.socc.Hawki.app.BuildingData;
import com.socc.Hawki.app.IndoorData;
import com.socc.Hawki.app.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import DataPacket.DataSource.DATAFORMAT;

/**
 * Created by joyeongje on 2016. 9. 4..
 */
public class Json {

    public static final int MAX_JSON_OBJECT = 50; // 최대 갯수

    public List<Data> load(JSONObject root, DATAFORMAT dataformat) throws JSONException {
        JSONObject jo = null;
        JSONArray dataArray = null;
        List<Data> datas = new ArrayList<Data>();
        Data proData = null;

        try {

            if (dataformat == DATAFORMAT.BuildingInfo)
                dataArray = root.getJSONArray("Build");

            else if (dataformat == DATAFORMAT.IndoorPosition) {
                jo = root.getJSONObject("position");
                proData = processIndoorJsonObject(jo);
                datas.add(proData);
            }

        } catch (JSONException e) { e.printStackTrace(); }


        if (dataArray != null) {

            int top = Math.min(MAX_JSON_OBJECT, dataArray.length());

            for (int i = 0; i < top; i++) {
                jo = dataArray.getJSONObject(i);

                switch (dataformat) {
                    case BuildingInfo:
                        proData = processBuildingJsonObject(jo);
                        break;

                }
                datas.add(proData);
            }

        }

        return datas;

    }

    public Data processBuildingJsonObject(JSONObject jo) throws JSONException {
        Data ma = null;

        if (jo.has("id") && jo.has("phone") && jo.has("title") && jo.has("address")) {

            ma = new BuildingData(jo.getString("id"), jo.getString("title"), null, null,
                    0, 0, jo.getString("phone"), jo.getString("address"));
        }

        return ma;

    }

    public Data processIndoorJsonObject(JSONObject jo) throws JSONException { // 실내위치

        Data ma = null;

        if (jo.has("x") && jo.has("y")) {
            ma = new IndoorData("", "", null, null,
                    jo.getString("x"), jo.getString("y"),"");
        }

        return ma;

    }


    public JSONObject createRssiJson(String bid, String x, String y, String z, List<ScanResult> scanResults) throws JSONException {
        JSONObject collecterData = new JSONObject();
        collecterData.put("bid", bid);
        collecterData.put("x", x);
        collecterData.put("y", y);
        collecterData.put("z", z);

        JSONArray rssiArray = new JSONArray();

        for (int i = 0; i < scanResults.size(); i++) {
            JSONObject rssidata = new JSONObject();
            rssidata.put("bssid", scanResults.get(i).BSSID);
            rssidata.put("dbm", scanResults.get(i).level);
            rssiArray.put(i, rssidata);
        }
        collecterData.put("rssi", rssiArray);

        return collecterData;
    }


    public JSONObject createRequestIndoorJson(String bid, List<ScanResult> scanResults) throws JSONException {
        JSONObject indoorData = new JSONObject();
        indoorData.put("bid", bid);

        JSONArray rssiArray = new JSONArray();

        for (int i = 0; i < scanResults.size(); i++) {
            JSONObject rssidata = new JSONObject();
            rssidata.put("bssid", scanResults.get(i).BSSID);
            rssidata.put("dbm", scanResults.get(i).level);
            rssiArray.put(i, rssidata);
        }

        indoorData.put("rssi", rssiArray);

        return indoorData;

    }

    public static String convertStandardJSONString(String data_json) {
        data_json = data_json.replace("\\\"", "\"");
        data_json = data_json.replace("\\\\", "\\");
        return data_json;
    }
}
