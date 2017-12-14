
package com.socc.Hawki.app.service.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Poi {

    @SerializedName("building_id")
    @Expose
    private Integer buildingId;
    @SerializedName("catefory")
    @Expose
    private String category;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("x")
    @Expose
    private Integer x;
    @SerializedName("y")
    @Expose
    private Integer y;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Poi() {
    }

    /**
     * 
     * @param id
     * @param name
     * @param y
     * @param url
     * @param x
     * @param buildingId
     */
    public Poi(Integer buildingId, String category, Integer id, String name, String url, Integer x, Integer y) {
        super();
        this.buildingId = buildingId;
        this.category = category;
        this.id = id;
        this.name = name;
        this.url = url;
        this.x = x;
        this.y = y;
    }

    public Integer getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Integer buildingId) {
        this.buildingId = buildingId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

}
