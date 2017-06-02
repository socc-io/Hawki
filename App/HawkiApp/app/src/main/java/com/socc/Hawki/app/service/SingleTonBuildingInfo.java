package com.socc.Hawki.app.service;

/**
 * Created by young on 2017-06-02.
 */

public class SingleTonBuildingInfo {
    private static final SingleTonBuildingInfo ourInstance = new SingleTonBuildingInfo();

    public static SingleTonBuildingInfo getInstance() {
        return ourInstance;
    }

    private String selectedBuildId;
    private String selectedBuildName;
    private SingleTonBuildingInfo() {

    }

    public String getSelectedBuildId() {
        return selectedBuildId;
    }

    public void setSelectedBuildId(String selectedBuildId) {
        this.selectedBuildId = selectedBuildId;
    }

    public String getSelectedBuildName() {
        return selectedBuildName;
    }

    public void setSelectedBuildName(String selectedBuildName) {
        this.selectedBuildName = selectedBuildName;
    }
}
