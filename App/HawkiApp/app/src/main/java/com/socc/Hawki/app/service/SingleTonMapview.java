package com.socc.Hawki.app.service;

import android.graphics.Bitmap;

/**
 * Created by young on 2017-06-02.
 */

public class SingleTonMapview {
    private static final SingleTonMapview ourInstance = new SingleTonMapview();

    public static SingleTonMapview getInstance() {
        return ourInstance;
    }

    private Bitmap currentBitmap;

    private SingleTonMapview() {

    }

    public Bitmap getCurrentBitmap() {
        return currentBitmap;
    }

    public void setCurrentBitmap(Bitmap currentBitmap) {
        this.currentBitmap = currentBitmap;
    }
}
