package com.example.msit;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.example.msit.overlayutil.DrivingRouteOverlay;
import com.example.msit.overlayutil.OverlayManager;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class IndexActivity extends AppCompatActivity implements OnGetRoutePlanResultListener, View.OnClickListener, OnGetGeoCoderResultListener {
    private MapView mapView;
    private BaiduMap mbaiduMap;
    private boolean isFirstLoc = true;//是否首次定位
    public LocationClient mLocationClient = null;
    private MyLocationListener myLocationListener = new MyLocationListener();
    private Button qiandaobtn;
    public static List<Activity> activityList = new LinkedList<Activity>();
    //添加覆盖物
    private Marker markerA, markerB;
    BitmapDescriptor bdA = BitmapDescriptorFactory.fromResource(R.mipmap.icon_marka);
    BitmapDescriptor bdB = BitmapDescriptorFactory.fromResource(R.mipmap.icon_markb);

    //弹出popwindow
    private InfoWindow mInfoWindow;

    // 搜索相关
    RoutePlanSearch mSearch = null;    // 搜索模块，也可去掉地图模块独立使用
    WalkingRouteResult nowResultwalk = null;
    BikingRouteResult nowResultbike = null;
    TransitRouteResult nowResultransit = null;
    DrivingRouteResult nowResultdrive = null;
    MassTransitRouteResult nowResultmass = null;
    OverlayManager routeOverlay = null;
    RouteLine route = null;
    private String city = "南昌";//当前城市
    private Button driverBtn;
    String startNodeStr = "八一广场";
    String endNodeStr = "南昌大学";
    int nodeIndex = -1; // 节点索引,供浏览节点时使用
    boolean useDefaultIcon = false;
    private EditText inputAddress;
    private Button btn;
    private double lat, lon;
    private String version, power, address, phone_name;
    private Set<String> set =new HashSet<>();
    private GeoCoder geo;
    private double lat_ed,lon_ed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_index);
        activityList.add(this);
        version = "当前系统版本：" + android.os.Build.VERSION.RELEASE;
        phone_name = android.os.Build.MODEL;
        mapView = (MapView) findViewById(R.id.mapview);
        btn = (Button) findViewById(R.id.btn_search);
        qiandaobtn = (Button) findViewById(R.id.add_qiandao);
        inputAddress = (EditText) findViewById(R.id.input_address);
        mbaiduMap = mapView.getMap();
        mbaiduMap.setMyLocationEnabled(true);

        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myLocationListener);
        //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
        geo = GeoCoder.newInstance();
        geo.setOnGetGeoCodeResultListener(this);
        btn.setOnClickListener(this);
        qiandaobtn.setOnClickListener(this);
        show();
        IntentFilter filter2 = new IntentFilter();
        filter2.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(br,filter2);



    }

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int current = intent.getExtras().getInt("level");
            int total = intent.getExtras().getInt("scale");// 获取总电量
            int percent = current * 100 / total;
            //tv.setText("电量剩余：" + percent + "%");
            String batteryinfo = "电量剩余：" + percent + "%";
            power = batteryinfo;
            Log.e("TAG", batteryinfo);
        }
    };

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(IndexActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;
            if (result.getRouteLines().size() > 1) {
                nowResultdrive = result;
                route = nowResultdrive.getRouteLines().get(0);
                DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mbaiduMap);
                mbaiduMap.setOnMarkerClickListener(overlay);
                routeOverlay = overlay;
                overlay.setData(nowResultdrive.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();

            } else if (result.getRouteLines().size() == 1) {
                route = result.getRouteLines().get(0);
                DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mbaiduMap);
                routeOverlay = overlay;
                mbaiduMap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
            } else {
                Log.d("route result", "结果数<0");
                return;
            }
        }
    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }

    // 天安门坐标
    double mLat1 = 39.915291;
    double mLon1 = 116.403857;
    // 百度大厦坐标
    double mLat2 = 40.056858;
    double mLon2 = 116.308194;
    /**
     * 启动百度地图导航(Native)
     */
    public void startNavi() {
        LatLng pt1 = new LatLng(lat, lon);
        LatLng pt2 = new LatLng(lat_ed, lon_ed);
//        PlanNode enNode = PlanNode.withCityNameAndPlaceName(city, endNodeStr);
//        LatLng pt2  = enNode.getLocation();
        // 构建 导航参数
       /* NaviParaOption para = new NaviParaOption()
                .startPoint(pt1).endPoint(pt2)
                .startName("天安门").endName("百度大厦");*/
        NaviParaOption para = new NaviParaOption()
                .startPoint(pt1).endPoint(pt2);

        try {
            BaiduMapNavigation.openBaiduMapNavi(para, this);
        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
           // showDialog();
        }

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_search) {
            if (TextUtils.isEmpty(inputAddress.getText())) {
                T.show("地址不能为空");
                return;
            } else {
                mbaiduMap.clear();
                // 处理搜索按钮响应
                // 设置起终点信息，对于tranist search 来说，城市名无意义
                // PlanNode stNode = PlanNode.withCityNameAndPlaceName(city, startNodeStr);
                PlanNode stNode = PlanNode.withLocation(new LatLng(lat, lon));
                PlanNode enNode = PlanNode.withCityNameAndPlaceName(city, inputAddress.getText().toString());
                mSearch.drivingSearch((new DrivingRoutePlanOption())
                        .from(stNode).to(enNode));
                nodeIndex = 1;

                /****/
                AlertDialog.Builder builder = new AlertDialog.Builder(IndexActivity.this);
                builder.setTitle("提示");
                builder.setMessage("是否需要导航？");
                builder.setNegativeButton("取消",null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Geo搜索
                        geo.geocode(new GeoCodeOption().city(
                                city).address(inputAddress.getText().toString()));

                    }
                });
                builder.show();
            }
        } else if (view.getId() == R.id.add_qiandao) {
            mbaiduMap.clear();
            LatLng ll = new LatLng(lat,
                    lon);
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(18.0f);
            mbaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
           if(true)return;
            /*****************大学生手机管理**********************/
            StringBuffer sb = new StringBuffer("");
            Iterator<String> iterator = set.iterator();
            while(iterator.hasNext()){
                sb.append(iterator.next()+"  ");
            }
            //提交签到数据
            final Request<String> request = NoHttp.createStringRequest(Constant.insertQiandaoUrl,RequestMethod.POST);
            request.add("version",version);
            request.add("power",power);
            request.add("address",address);
            request.add("phone_name",phone_name);
            request.add("name",Constant.user.getName());
            request.add("app_name",sb.toString());
            Constant.getQueueInstance().add(0x123, request, new OnResponseListener<String>() {
                @Override
                public void onStart(int what) {

                }

                @Override
                public void onSucceed(int what, Response<String> response) {
                    if("true".equals(response.get())){
                        T.show("签到成功");
                    }
                    else{
                        T.show("由于网络原因，签到失败");
                    }
                }

                @Override
                public void onFailed(int what, Response<String> response) {

                }

                @Override
                public void onFinish(int what) {

                }
            });

         }
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(IndexActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
      /*  mBaiduMap.clear();
        mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_marka)));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
                .getLocation()));*/
        String strInfo = String.format("纬度：%f 经度：%f",
                result.getLocation().latitude, result.getLocation().longitude);
       // Toast.makeText(GeoCoderDemo.this, strInfo, Toast.LENGTH_LONG).show();
        lat_ed = result.getLocation().latitude;
        lon_ed = result.getLocation().longitude;

        startNavi();
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

    }

    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || mapView == null)
                return;
            city = location.getCity();
            lat = location.getLatitude();
            lon = location.getLongitude();
            Log.e("TAG", location.getAddrStr() + ":" + location.getLatitude() + ":" + location.getLongitude());
            //28.659747:115.956578
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mbaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                address = location.getAddrStr();
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mbaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    // 定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.mipmap.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.mipmap.icon_en);
            }
            return null;
        }
    }


    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mLocationClient.stop();
        mbaiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
        mapView = null;
        bdA.recycle();
        bdB.recycle();
        super.onDestroy();
    }


    /**
     * 获取当前应用名称
     *
     * @param context
     * @return
     */
    public static String getAppName(Context context) {
        String appName = null;
        PackageManager packageManager = context.getPackageManager();
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            //CharSequence这两者效果是一样的.
            appName = packageManager.getApplicationLabel(applicationInfo).toString();
            appName = (String) packageManager.getApplicationLabel(applicationInfo);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("GsonUtils", "Exception=" + e.toString());
            return null;
        }
        return appName;
    }


    //正在运行的 
    public List<String> getRunningProcess() {
        PackagesInfo pi = new PackagesInfo(this);
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        //获取正在运行的应用
        List<ActivityManager.RunningAppProcessInfo> run = am.getRunningAppProcesses();
        //获取包管理器，在这里主要通过包名获取程序的图标和程序名
        PackageManager pm = this.getPackageManager();
        List<String> list = new ArrayList<String>();

        for (ActivityManager.RunningAppProcessInfo ra : run) {
            //这里主要是过滤系统的应用和电话应用，当然你也可以把它注释掉。
            if (ra.processName.equals("system") || ra.processName.equals("com.Android.phone")) {
                continue;
            }
            // Programe  pr = new Programe();
            //   pr.setIcon(pi.getInfo(ra.processName).loadIcon(pm));
            //   pr.setName(pi.getInfo(ra.processName).loadLabel(pm).toString());
            //  System.out.println(pi.getInfo(ra.processName).loadLabel(pm).toString());
            Log.e("TAG", pi.getInfo(ra.processName).loadLabel(pm).toString());
            list.add(pi.getInfo(ra.processName).loadLabel(pm).toString());
        }

        return list;
    }

    /***
     * 获取后台进程中所有的应用名
     */
    public void show() {
        List<ActivityManager.RunningAppProcessInfo> runningAppsInfo = new ArrayList<ActivityManager.RunningAppProcessInfo>();
        PackageManager pm = this.getPackageManager();
        ActivityManager am = (ActivityManager) this
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = am
                .getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo service : runningServices) {
            String pkgName = service.process.split(":")[0];
            try {
                ActivityManager.RunningAppProcessInfo item = new ActivityManager.RunningAppProcessInfo();
                item.pkgList = new String[]{pkgName};
                item.pid = service.pid;
                item.processName = service.process;
                item.uid = service.uid;
                String name = ApplicationUtil.getProgramNameByPackageName(IndexActivity.this,pkgName);
                set.add(name);
                runningAppsInfo.add(item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
