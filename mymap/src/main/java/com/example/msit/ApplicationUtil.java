package com.example.msit;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by wls on 2017/3/11 16:00.
 */

public class ApplicationUtil {
                public static String getProgramNameByPackageName(Context context,String packageName) {
                PackageManager pm = context.getPackageManager();
                String name = null;
                try {
                        name = pm.getApplicationLabel(pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA)).toString();
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                return name;
            }
}
