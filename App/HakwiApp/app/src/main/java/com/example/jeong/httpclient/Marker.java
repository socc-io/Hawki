package com.example.jeong.httpclient;

/**
 * Created by joyeongje on 2016. 9. 4..
 */
public class Marker {

    private String BuildId; // 건물 id
    private String title; // 마커의 이름
    private String description; // 마커의 내용
    private String URL; // 연동 가능한 유알엘

    Marker(){};

    Marker(String BuildId, String title, String description, String url) {
        this.BuildId = BuildId;
        this.title = title;
        this.description = description;
        this.URL = url;
    }

    public String getBuildId() {
        return BuildId;
    }

    public void setBuildId(String buildId) {
        BuildId = buildId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}
