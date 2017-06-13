package com.socc.Hawki.app.service;

import android.net.wifi.ScanResult;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.socc.Hawki.app.service.request.PostCollectRssiReq;
import com.socc.Hawki.app.service.request.PostGetPositionReq;
import com.socc.Hawki.app.service.response.GetBuildingInfoRes;
import com.socc.Hawki.app.service.response.PostGetPositionRes;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by gim-yeongjin on 2017. 5. 15..
 */

public class HawkAPI extends APIAgent {
    final static private HawkAPI _global = new HawkAPI("http://smilu.link:4000");

    private HawkAPI(String baseURL) {
        super(baseURL);
    }
    static public HawkAPI getInstance() {
        return _global;
    }

    public String getMapImageURL(String buildId) {
        return baseURL + "/static/map/" + buildId + ".jpg";
    }


    public List<GetBuildingInfoRes> getBuildingInfo(String buildingName) {
        String resString = null;

         try {
             resString = new HawkAPI(baseURL).execute("/buildinginfo?buildName=" + buildingName, "GET", null).get();
         } catch (InterruptedException | ExecutionException e) {
             e.printStackTrace();
         }

        // Log.d("건물정보", resString);
        if(resString == null) return null;

        JsonObject response = (new JsonParser()).parse(resString).getAsJsonObject();
        JsonArray  buildings = response.get("Build").getAsJsonArray();

        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<GetBuildingInfoRes>>(){}.getType();
        List<GetBuildingInfoRes> res = gson.fromJson(buildings, listType);

        return res;
    }

    public String postCollectRssi(String bid, float x, float y, float z, List<ScanResult> scanResult) {
        PostCollectRssiReq req = new PostCollectRssiReq(bid, x, y, z, scanResult);
        Gson gson = new Gson();

        try {
            String resString = new HawkAPI(baseURL).execute("/collectrssi", "POST", gson.toJson(req)).get();
            return resString;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    public PostGetPositionRes postGetPosition(String bid, List<ScanResult> scanResults) {

        PostGetPositionReq req = new PostGetPositionReq(bid, scanResults);
        Gson gson = new Gson();
        String response = null;

        try {
             response =  new HawkAPI(baseURL).execute("/getposition", "POST", gson.toJson(req)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        if(response == null) return null;
        JsonObject resJson = (new JsonParser()).parse(response).getAsJsonObject();
        PostGetPositionRes res = gson.fromJson(resJson, PostGetPositionRes.class);

        return res;
    }
}
