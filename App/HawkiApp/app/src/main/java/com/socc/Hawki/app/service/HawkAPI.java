package com.socc.Hawki.app.service;

import android.net.wifi.ScanResult;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.socc.Hawki.app.model.BuildingData;

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

    public List<BuildingData> getBuildingInfo(String buildingName) {
        String resString = this.getHttpResponse("/buildinginfo?buildName=" + buildingName, "GET", null);
        JsonObject res = (new JsonParser()).parse(resString).getAsJsonObject();
        if (res != null) {
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
        }
        return null;
    }

    public String postCollectRssi(String bid, String x, String y, String z, List<ScanResult> scanResult) {
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

        return this.getHttpResponse("/collectrssi", "POST", body.toString());
    }
}
