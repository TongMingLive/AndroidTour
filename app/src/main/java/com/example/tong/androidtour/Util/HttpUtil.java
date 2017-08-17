package com.example.tong.androidtour.Util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class HttpUtil {
	//public static String pathstr = "http://naturemix.cn/servlet/";
	//public static String pathstr = "http://marvel.ac.cn/servlet/";
	public static String pathstr = "http://192.168.0.104:8080/";
	//public static String pathstr = "http://10.0.2.2:8080/";
	public static String path = pathstr;
	
	public static String doGet(String url, Map<String, Object> oMap) {
		String info = null;
		StringBuilder builder = new StringBuilder(url);
		builder.append("?");
		for (Map.Entry<String, Object> entry : oMap.entrySet()) {
			String key = entry.getKey();
			Object values = entry.getValue();
			builder.append(key).append("=").append(values.toString()).append("&");
		}
		builder.deleteCharAt(builder.length() - 1);

		try {
			HttpClient client = new DefaultHttpClient();

			HttpGet get = new HttpGet(builder.toString());
			HttpResponse response = client.execute(get);

			int code = response.getStatusLine().getStatusCode();
			if (code == 200) {
				InputStream is = response.getEntity().getContent();

				info = StreamUtil.InputSteamToString(is);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return info;
	}


	public static String doPost(String url,Map<String, Object> oMap){
		String info=null;
		try {
			HttpClient client=new DefaultHttpClient();
			
			HttpPost post=new HttpPost(url);
			
			List<NameValuePair> oList=new ArrayList<NameValuePair>();
			for (Map.Entry<String, Object> entry : oMap.entrySet()) {
				NameValuePair pair=new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
				
				oList.add(pair);
			}
			
			HttpEntity entity=new UrlEncodedFormEntity(oList,"UTF-8");
			post.setEntity(entity);
			
			HttpResponse response=client.execute(post);
			int code=response.getStatusLine().getStatusCode();
			if (code==200) {
				InputStream is=response.getEntity().getContent();
				
				info=StreamUtil.InputSteamToString(is);
			}
			
		} catch (Exception e) {
			return "error";
		}
		return info;
	}
	public static String sendGetRequest2(String path, Map<String, String> params)
			throws Exception {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();// ����������
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                pairs.add(new BasicNameValuePair(entry.getKey(), entry
                        .getValue()));
            }
        }
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs, "utf-8"); 
		HttpPost httpPost = new HttpPost(path); //path:����·��
        httpPost.setEntity(entity);
		HttpClient httpClient=new DefaultHttpClient();
		HttpResponse response = httpClient.execute(httpPost);
        if (response.getStatusLine().getStatusCode() == 200) {
        	InputStream in=response.getEntity().getContent();
        	int len;
			byte[] buffer = new byte[1024];
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			while ((len = in.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
			}
			String s = bos.toString();
			System.out.println(s + "------");
			return URLDecoder.decode(s,"utf-8");
        }
        else{
        	return "error";
        }
	}
}
