package com.example.tong.androidtour.Util;

import java.io.IOException;
import java.io.InputStream;

public class StreamUtil {

	
	public static String InputSteamToString(InputStream is){
		String info=null;
		try {
			StringBuilder builder=new StringBuilder();
			int has=0;
			byte[] buffer=new byte[1024];
			while ((has=is.read(buffer))!=-1) {
				builder.append(new String(buffer,0,has));
			}
			is.close();
			
			info=builder.toString();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return info;
	}
}
