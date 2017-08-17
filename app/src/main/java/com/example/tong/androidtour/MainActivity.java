package com.example.tong.androidtour;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.example.tong.androidtour.Fragment.Fragment1;
import com.example.tong.androidtour.Fragment.Fragment2;
import com.example.tong.androidtour.Fragment.Fragment3;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {
    private long exitTime = 0;
    private BottomNavigationView navigation;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private List<Fragment> list = new ArrayList<>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            fragmentTransaction = getSupportFragmentManager()
                    .beginTransaction();
            fragmentTransaction.hide(list.get(0));
            fragmentTransaction.hide(list.get(1));
            fragmentTransaction.hide(list.get(2));
            fragmentTransaction.commit();

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragmentTransaction.show(list.get(0));
                    return true;
                case R.id.navigation_explore:
                    fragmentTransaction.show(list.get(1));
                    return true;
                case R.id.navigation_dashboard:
                    fragmentTransaction.show(list.get(2));
                    return true;
            }
            fragmentTransaction.commit();
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //获取fragment管理者对象
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        list.add(new Fragment1());
        list.add(new Fragment2());
        list.add(new Fragment3());

        fragmentTransaction.add(R.id.content,list.get(0));
        fragmentTransaction.add(R.id.content,list.get(1));
        fragmentTransaction.hide(list.get(1));
        fragmentTransaction.add(R.id.content,list.get(2));
        fragmentTransaction.hide(list.get(2));

        fragmentTransaction.show(list.get(0));
        fragmentTransaction.commit();


        if (Build.VERSION.SDK_INT >= 23) {
            int checkPermission = ContextCompat.checkSelfPermission(
                    MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
            if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        MainActivity.this, new String[]{
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        }, 1);
                ActivityCompat.requestPermissions(
                        MainActivity.this, new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION
                        }, 1);
                Log.d("TTTT", "弹出提示");
            }
        }

    }

    //再次返回键退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Snackbar.make(navigation, "再按一次退出程序", Snackbar.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
