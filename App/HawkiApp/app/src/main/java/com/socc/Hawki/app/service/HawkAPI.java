package com.socc.Hawki.app.service;

import android.net.wifi.ScanResult;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.socc.Hawki.app.model.BuildingData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gim-yeongjin on 2017. 5. 15..
 */

public class HawkAPI extends APIAgent {
    final static private HawkAPI _global = new HawkAPI("http://beaver.hp100.net:4000");

    private HawkAPI(String baseURL) {
        super(baseURL);
    }
    static public HawkAPI getInstance() {
        return _global;
    }

    public String getMapImageURL(String buildId) {
        return baseURL + "/static/map/" + buildId + ".jpg";
    }

    public List<BuildingData> getBuildingInfo(String buildingName) {
        String resString = this.getHttpResponse("/buildinginfo?buildName=" + buildingName, "GET", null);
        if(resString == null) return null;

        JsonObject res = (new JsonParser()).parse(resString).getAsJsonObject();
        ArrayList<BuildingData> resultData = new ArrayList<>();
        JsonArray  buildings = res.getAsJsonArray("Build");

        for(JsonElement buildElement: buildings) {
            JsonObject build = buildElement.getAsJsonObject();
            resultData.add(new BuildingData(
                    build.getAsJsonObject("id").getAsString(),
                    build.getAsJsonObject("title").getAsString(),
                    null,
                    null,
                    0,
                    0,
                    build.getAsJsonObject("phone").getAsString(),
                    build.getAsJsonObject("address").getAsString()
            ));
        }
        return resultData;
    }

    public JsonObject postCollectRssi(String bid, float x, float y, float z, List<ScanResult> scanResult) {
        JsonObject body = new JsonObject();
        body.addProperty("bid", bid);
        body.addProperty("x", x);
        body.addProperty("y", y);
        body.addProperty("z", z);

        JsonArray rssi = new JsonArray();
        for(ScanResult scan: scanResult) {
            JsonObject scanJson = new JsonObject();
            scanJson.addProperty("bssid", scan.BSSID);
            scanJson.addProperty("dbm", scan.level);

            rssi.add(scanJson);
        }
        body.add("rssi", rssi);

        String res = this.getHttpResponse("/collectrssi", "POST", body.toString());
        if (res == null) return null;
        JsonObject resJson = (new JsonParser()).parse(res).getAsJsonObject();

        return resJson;
    }

    public JsonObject postGetPosition(String bid, List<ScanResult> scanResults) {
        JsonObject body = new JsonObject();
        body.addProperty("bid", bid);

        JsonArray rssiArray = new JsonArray();
        for(ScanResult result: scanResults) {
            JsonObject sc = new JsonObject();
            sc.addProperty("bssid", result.BSSID);
            sc.addProperty("dbm", result.level);
            rssiArray.add(sc);
        }
        body.add("rssi", rssiArray);

        String res = this.getHttpResponse("/getposition", "POST", body.toString());
        if(res == null) return null;
        JsonObject resJson = (new JsonParser()).parse(res).getAsJsonObject();

        return resJson;
    }
}
