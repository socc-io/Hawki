package com.socc.Hawki.app;

/**
 * Created by joyeongje on 2016. 9. 4..
 */
public class Data {

    private String BuildId;
    private String title;
    private String description;
    private String URL;

    Data(){}

    Data(String BuildId, String title, String description, String url) {
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
