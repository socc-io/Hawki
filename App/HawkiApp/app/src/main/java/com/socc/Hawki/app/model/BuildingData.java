package com.socc.Hawki.app.model;

import com.google.gson.JsonObject;

/**
 * Created by joyeongje on 2016. 9. 4..
 */
public class BuildingData extends RecvData {

    private Double latitude;
    private Double longitude;
    private String phoneNumber;
    private String address;

    public BuildingData(){};

    public BuildingData(String bid, String title, String description, String url, double latitude, double longitude,
                        String phoneNumber, String address) {

        super(bid,title,description,url);
        this.latitude = latitude;
        this.longitude = longitude;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void mapFromJson(JsonObject json) {

    }
}
