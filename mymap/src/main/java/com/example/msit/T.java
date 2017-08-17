package com.example.msit;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by wls on 2017/3/10 13:39.
 */

public class T {
    private static Context context;
    public T(Context context){
        this.context = context;
    }
    public static void show(String mess){
        if(context != null)
            Toast.makeText(context,mess,Toast.LENGTH_LONG).show();
    }
}
