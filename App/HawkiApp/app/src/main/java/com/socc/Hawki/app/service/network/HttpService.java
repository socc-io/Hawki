package com.socc.Hawki.app.service.network;

import com.socc.Hawki.app.service.request.PostCollectRssiReq;
import com.socc.Hawki.app.service.request.PostGetPositionReq;
import com.socc.Hawki.app.service.response.GetBuildingInfoRes;
import com.socc.Hawki.app.service.response.GetPoiListReq;
import com.socc.Hawki.app.service.response.PostGetPositionRes;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by suno on 2017. 8. 18..
 */

public interface HttpService {
    @Headers("Content-type: application/json; charset=utf-8")
    @GET("buildinginfo")
    Call<GetBuildingInfoRes> getBuildingInfo(@Query(value = "buildName", encoded = true) String buildName);

    @Headers("Content-type: application/json; charset=utf-8")
    @POST("collectrssi")
    Call<JSONObject> postCollectRssi(@Body PostCollectRssiReq req);

    @Headers("Content-type: application/json; charset=utf-8")
    @POST("getposition")
    Call<PostGetPositionRes> postGetPosition(@Body PostGetPositionReq req);

    @Headers("Content-type: application/json; charset=utf-8")
    @GET("building/{id}/poi")
    Call<GetPoiListReq> getPOIList(@Path(value="id", encoded = true) String id);
}