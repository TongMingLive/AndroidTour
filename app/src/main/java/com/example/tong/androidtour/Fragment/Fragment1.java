package com.example.tong.androidtour.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.tong.androidtour.Bean.Messeng;
import com.example.tong.androidtour.MainActivity;
import com.example.tong.androidtour.MessengAll;
import com.example.tong.androidtour.MessengPage;
import com.example.tong.androidtour.R;
import com.example.tong.androidtour.Util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by Tong on 2017/3/16.
 */

public class Fragment1 extends Fragment{
    private ViewPager vp;
    private PagerTabStrip pts;
    private String[] titles = new String[]{"景点", "酒店", "美食"};
    private List<View> list = new ArrayList<>();
    String v1Str,v2Str,v3Str;
    private ListView lv1,lv2,lv3;
    private List<Messeng> v1list = new ArrayList<>();
    private List<Messeng> v2list = new ArrayList<>();
    private List<Messeng> v3list = new ArrayList<>();
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main,null);

        searchView = (SearchView) view.findViewById(R.id.main_select_btn);
        vp = (ViewPager) view.findViewById(R.id.main_vp);
        pts = (PagerTabStrip) view.findViewById(R.id.main_pts);

        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent();
                intent.setClass(getContext(), MessengAll.class);
                intent.putExtra("query",query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        pts.setTextColor(Color.BLUE);
        pts.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
        pts.setTabIndicatorColor(Color.BLUE);

        View v1 = inflater.inflate(R.layout.messeng_all, null);
        View v2 = inflater.inflate(R.layout.messeng_all, null);
        View v3 = inflater.inflate(R.layout.messeng_all, null);

        lv1 = (ListView) v1.findViewById(R.id.messeng_lv);
        lv2 = (ListView) v2.findViewById(R.id.messeng_lv);
        lv3 = (ListView) v3.findViewById(R.id.messeng_lv);


        new Thread(){
            @Override
            public void run() {
                super.run();
                Map<String,Object> map1 =new HashMap<>();
                map1.put("messengType","0");
                v1Str = HttpUtil.doPost(HttpUtil.path+"SelectMessengServlet",map1);
                Map<String,Object> map2 =new HashMap<>();
                map2.put("messengType","1");
                v2Str = HttpUtil.doPost(HttpUtil.path+"SelectMessengServlet",map2);
                Map<String,Object> map3 =new HashMap<>();
                map3.put("messengType","2");
                v3Str = HttpUtil.doPost(HttpUtil.path+"SelectMessengServlet",map3);
                if (v1Str.equals("error") || v2Str.equals("error") || v3Str.equals("error")) {
                    handler.sendEmptyMessage(0x000);
                }else {
                    handler.sendEmptyMessage(0x123);
                }
            }
        }.start();


        list.add(v1);
        list.add(v2);
        list.add(v3);

        //设置关联适配器
        myAdapter adapter = new myAdapter();
        vp.setAdapter(adapter);

        return view;
    }

    private class v1Adapte extends BaseAdapter{

        @Override
        public int getCount() {
            return v1list.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View v = layoutInflater.inflate(R.layout.messeng_item, null);
            LinearLayout layout = (LinearLayout) v.findViewById(R.id.messeng_ll);
            LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.messeng_lin);
            TextView title = (TextView) v.findViewById(R.id.messeng_item_title);
            TextView page = (TextView) v.findViewById(R.id.messeng_item_page);

            layout.setBackgroundResource(R.drawable.mes);
            title.setText(v1list.get(position).getMessengName());
            page.setText(v1list.get(position).getMessengTitle());

            linearLayout.setTag(position);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag();
                    Intent intent = new Intent();
                    intent.setClass(getContext(),MessengPage.class);
                    intent.putExtra("messeng",v1list.get(position));
                    startActivity(intent);
                }
            });

            return v;
        }
    }

    private class v2Adapte extends BaseAdapter{

        @Override
        public int getCount() {
            return v2list.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View v = layoutInflater.inflate(R.layout.messeng_item, null);
            LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.messeng_lin);
            LinearLayout layout = (LinearLayout) v.findViewById(R.id.messeng_ll);
            TextView title = (TextView) v.findViewById(R.id.messeng_item_title);
            TextView page = (TextView) v.findViewById(R.id.messeng_item_page);

            layout.setBackgroundResource(R.drawable.hotel);
            title.setText(v2list.get(position).getMessengName());
            page.setText(v2list.get(position).getMessengTitle());

            linearLayout.setTag(position);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag();
                    Intent intent = new Intent();
                    intent.setClass(getContext(),MessengPage.class);
                    intent.putExtra("messeng",v2list.get(position));
                    startActivity(intent);
                }
            });

            return v;
        }
    }

    private class v3Adapte extends BaseAdapter{

        @Override
        public int getCount() {
            return v3list.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View v = layoutInflater.inflate(R.layout.messeng_item, null);
            LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.messeng_lin);
            LinearLayout layout = (LinearLayout) v.findViewById(R.id.messeng_ll);
            TextView title = (TextView) v.findViewById(R.id.messeng_item_title);
            TextView page = (TextView) v.findViewById(R.id.messeng_item_page);

            layout.setBackgroundResource(R.drawable.foot);
            title.setText(v3list.get(position).getMessengName());
            page.setText(v3list.get(position).getMessengTitle());

            linearLayout.setTag(position);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag();
                    Intent intent = new Intent();
                    intent.setClass(getContext(),MessengPage.class);
                    intent.putExtra("messeng",v3list.get(position));
                    startActivity(intent);
                }
            });

            return v;
        }
    }

    private class myAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //把目标View添加到容器里面
            container.addView(list.get(position));
            return list.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //从容器中移除上一个View对象
            container.removeView(list.get(position));
            //super.destroyItem(container, position, object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123){
                ListAdapter la1 = new v1Adapte();
                ListAdapter la2 = new v2Adapte();
                ListAdapter la3 = new v3Adapte();
                
                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                JsonArray jsonArray1 = parser.parse(v1Str).getAsJsonArray();
                for (JsonElement json : jsonArray1) {
                    v1list.add(gson.fromJson(json , Messeng.class));
                }
                JsonArray jsonArray2 = parser.parse(v2Str).getAsJsonArray();
                for (JsonElement json : jsonArray2) {
                    v2list.add(gson.fromJson(json , Messeng.class));
                }
                JsonArray jsonArray3 = parser.parse(v3Str).getAsJsonArray();
                for (JsonElement json : jsonArray3) {
                    v3list.add(gson.fromJson(json , Messeng.class));
                }
                lv1.setAdapter(la1);
                lv2.setAdapter(la2);
                lv3.setAdapter(la3);
            }else if (msg.what == 0x000){
                Snackbar.make(vp, "网络连接失败，请检查您的网络", Snackbar.LENGTH_LONG).show();
            }
        }
    };

}
