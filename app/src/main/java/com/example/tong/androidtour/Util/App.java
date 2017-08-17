package com.example.tong.androidtour.Util;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.example.tong.androidtour.Bean.User;

/**
 * Created by flytoyou on 2017/3/1.
 */

public class App extends Application {
    public static User user;
    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);
    }
}
