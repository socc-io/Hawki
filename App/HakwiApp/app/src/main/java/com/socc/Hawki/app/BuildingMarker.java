package com.socc.Hawki.app;

/**
 * Created by joyeongje on 2016. 9. 4..
 */
public class BuildingMarker extends Marker{

    public Double latitude;
    public Double longitude;
    private String phoneNumber;
    public String address;

    public BuildingMarker(){};

    public BuildingMarker(String bid, String title,String description,String url, double latitude, double longitude,
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
}
