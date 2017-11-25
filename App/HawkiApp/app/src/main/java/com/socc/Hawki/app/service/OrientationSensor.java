package com.socc.Hawki.app.service;

/**
 * Created by kakaogames on 2017. 11. 25..
 */

public class OrientationSensor {
    private float x;
    private float y;
    private float z;

    public OrientationSensor(float x, float y, float z) {
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
