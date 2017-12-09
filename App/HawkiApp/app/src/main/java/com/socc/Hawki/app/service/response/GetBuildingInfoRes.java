package com.socc.Hawki.app.service.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by suno on 2017. 8. 18.
 */

public class GetBuildingInfoRes {
    @SerializedName("Build")
    List<Build> buildList;

    public List<Build> getBuildList() {
        return buildList;
    }

    public void setBuildList(List<Build> buildList) {
        this.buildList = buildList;
    }
}
