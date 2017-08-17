package com.example.msit;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.RequestQueue;

/**
 * Created by wls on 2017/2/24 23:09.
 */

public class Constant {
    public final static String url="http://192.168.2.114:8080/zijiayou/";
    public final static String httpurl=url+"servlet/";
    public final static String loginUrl=httpurl+"LoginServlet";
    public final static String regUrl=httpurl+"RegeistServlet";
    public final static String insertQiandaoUrl=httpurl+"InsertPhoneServlet";
    public  static User user;
    public static String city;
    //请求队列
    private static RequestQueue quee = null;

    /***
     * 获取请求队列对象
     * @return RequestQueue
     */
    public static RequestQueue getQueueInstance(){
        if(quee == null){
            quee = NoHttp.newRequestQueue();
        }
        return quee;
    }
}
