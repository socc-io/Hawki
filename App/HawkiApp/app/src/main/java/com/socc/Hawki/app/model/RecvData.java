package com.socc.Hawki.app.model;

/**
 * Created by joyeongje on 2016. 9. 4..
 */
public class RecvData {

    private String buildId;
    private String title;
    private String description;
    private String URL;

    RecvData(){}

    RecvData(String BuildId, String title, String description, String url) {
        this.buildId = BuildId;
        this.title = title;
        this.description = description;
        this.URL = url;
    }

    public String getBuildId() {
        return buildId;
    }

    public void setBuildId(String buildId) {
        this.buildId = buildId;
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
