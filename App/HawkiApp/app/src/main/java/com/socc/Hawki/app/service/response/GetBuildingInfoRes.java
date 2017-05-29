package com.socc.Hawki.app.service.response;

/**
 * Created by gim-yeongjin on 2017. 5. 29..
 */

public class GetBuildingInfoRes implements HawkResponse {
    String address;
    String id;
    String phone;
    String title;

    public GetBuildingInfoRes(String address, String id, String phone, String title) {
        this.address = address;
        this.id = id;
        this.phone = phone;
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
