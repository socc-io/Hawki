package com.socc.Hawki.app.service.response;

/**
 * Created by gim-yeongjin on 2017. 5. 29..
 */

public class PostGetPositionRes implements HawkResponse {
    float x, y, z;

    public PostGetPositionRes(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }
}
