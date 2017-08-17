package com.example.msit;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.yolanda.nohttp.NoHttp;

/**
 * Created by wls on 2017/3/10 14:05.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        new T(getApplicationContext());
        SDKInitializer.initialize(getApplicationContext());
        NoHttp.initialize(this, new NoHttp.Config()
                // 设置全局连接超时时间，单位毫秒
                .setConnectTimeout(30 * 1000)
                // 设置全局服务器响应超时时间，单位毫秒
                .setReadTimeout(30 * 1000));

    }

}
