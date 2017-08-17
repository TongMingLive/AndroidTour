package com.example.tong.androidtour.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.tong.androidtour.IndexActivity;
import com.example.tong.androidtour.R;

/**
 * Created by Tong on 2017/3/17.
 */

public class Fragment2 extends Fragment {
    //private WebView webView;
    private Button button;

    public  MapView bmapView = null;
    public BaiduMap baiduMap = null;

    //定位的相关声明
    public LocationClient LocationClient = null;

    //自定义图标
    BitmapDescriptor mCurrentMark = null;
    boolean isFirstLoc = true; //是否是首次定位

    public BDLocationListener myListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //MapView 销毁后不再处理 接收新的位置
            if(location==null||bmapView==null) {

                return;
            }

            MyLocationData locData = new MyLocationData.Builder().accuracy
                    (location.getRadius()).direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();


            //设置定位数据
            baiduMap.setMyLocationData(locData);

            baiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                    MyLocationConfiguration.LocationMode.FOLLOWING,true, BitmapDescriptorFactory.fromResource(0)));

            if(isFirstLoc) { //第一次定位

                isFirstLoc=!isFirstLoc; //改变值  如果是false 则进不来了

                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());


                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 16);   //设置地图中心点以及 缩放级别
                baiduMap.animateMapStatus(u);
            }

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map,null);
        bmapView = (MapView) view.findViewById(R.id.bmapView);

        bmapView.showZoomControls(true); //设置是否显示缩放控件
        baiduMap=bmapView.getMap();

        //开启定位图层
        baiduMap.setMyLocationEnabled(true);
        LocationClient = new LocationClient(getActivity().getApplicationContext());//实例化 LocationClient类
        LocationClient.registerLocationListener(myListener);//注册监听函数
        //设置定位参数
        this.setLocationOption();
        LocationClient.start(); //开启定位

        button = (Button) view.findViewById(R.id.bmap_btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), IndexActivity.class),0);
            }
        });

        /*webView = (WebView) view.findViewById(R.id.mapview);

        String url = "http://m.amap.com";
        webView.loadUrl(url);
        //启用支持javascript
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        //设置不让跳转系统浏览器
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                return false;
            }
        });
        //开启webView定位

        //启用数据库
        settings.setDatabaseEnabled(true);
        String dir = getActivity().getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        //启用地理定位
        settings.setGeolocationEnabled(true);
        //设置定位的数据库路径
        settings.setGeolocationDatabasePath(dir);
        //最重要的方法，一定要设置，这就是出不来的主要原因
        settings.setDomStorageEnabled(true);
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }
        });*/

        return view;
    }

    /**
     * 设置定位参数
     */
    private void setLocationOption() {

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); //打开Gps
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置成定位模式
        option.setCoorType("bd09ll"); //返回的结果 是百度的经纬度，默认值 gcj02
        option.setScanSpan(5000);//设置发起定位 请求的时间隔为5000ms
        option.setIsNeedAddress(true);//返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);//返回的定位结果 包含手机机头的方向
        LocationClient.setLocOption(option);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bmapView.onDestroy();
        bmapView=null;
    }

    @Override
    public void onResume() {
        super.onResume();
        bmapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        bmapView.onPause();
    }
}
