package com.socc.Hawki.app.application;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by suno on 2017. 8. 18..
 */

public class GlobalApplication extends Application {
    private static volatile GlobalApplication mInstance = null;
    private static volatile Activity currentActivity = null;

    private static Retrofit retrofit = null;
    private static final String BASE_URL = "http://hawki.smilu.link:4000";

    public static GlobalApplication getGlobalApplicationContext() {
        if(mInstance == null)
            throw new IllegalStateException("this application does not inherit GlobalApplication");
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        Stetho.initializeWithDefaults(this);
    }

    @Override
    public void onTerminate() {
        mInstance = null;

        super.onTerminate();
    }

    public static Activity getCurrentActivity() {
        Log.d("TAG", "++ currentActivity : " + (currentActivity != null ? currentActivity.getClass().getSimpleName() : ""));
        return currentActivity;
    }

    public static void setCurrentActivity(Activity currentActivity) {
        GlobalApplication.currentActivity = currentActivity;
    }

    public static Retrofit getRetrofit(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .client(new OkHttpClient().newBuilder()
                            .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                            .addNetworkInterceptor(new StethoInterceptor()).build())
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }

    public static String getMapImageURL(String buildId) {
        return BASE_URL + "/static/map/" + buildId + ".jpg";
    }

}
