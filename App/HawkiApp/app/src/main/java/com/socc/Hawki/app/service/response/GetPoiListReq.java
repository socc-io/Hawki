
package com.socc.Hawki.app.service.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetPoiListReq {

    @SerializedName("pois")
    @Expose
    private List<Poi> pois = null;
    @SerializedName("success")
    @Expose
    private Integer success;

    /**
     * No args constructor for use in serialization
     * 
     */
    public GetPoiListReq() {
    }

    /**
     * 
     * @param pois
     * @param success
     */
    public GetPoiListReq(List<Poi> pois, Integer success) {
        super();
        this.pois = pois;
        this.success = success;
    }

    public List<Poi> getPois() {
        return pois;
    }

    public void setPois(List<Poi> pois) {
        this.pois = pois;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

}
