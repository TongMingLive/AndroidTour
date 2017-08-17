package com.example.tong.androidtour;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tong.androidtour.Bean.Messeng;
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
 * Created by tong on 17-3-28.
 */

public class MessengAll extends AppCompatActivity {
    private ListAdapter adapter = new myAdapte();
    private List<Messeng> list = new ArrayList<>();
    private ListView listView;
    private String str;
    private String messengName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messeng_all);

        messengName = getIntent().getStringExtra("query");
        listView = (ListView) findViewById(R.id.messeng_lv);


        new Thread(){
            @Override
            public void run() {
                super.run();
                Map<String,Object> map =new HashMap<>();
                map.put("messengName",messengName);
                str = HttpUtil.doPost(HttpUtil.path+"SelectMessengByNameServlet",map);
                if (str.equals("error")) {
                    handler.sendEmptyMessage(0x000);
                }else {
                    handler.sendEmptyMessage(0x123);
                }
            }
        }.start();
    }

    private class myAdapte extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
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
            LayoutInflater layoutInflater = LayoutInflater.from(MessengAll.this);
            View v = layoutInflater.inflate(R.layout.messeng_item, null);
            LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.messeng_lin);
            LinearLayout layout = (LinearLayout) v.findViewById(R.id.messeng_ll);
            TextView title = (TextView) v.findViewById(R.id.messeng_item_title);
            TextView page = (TextView) v.findViewById(R.id.messeng_item_page);

            layout.setBackgroundResource(R.drawable.foot);
            title.setText(list.get(position).getMessengName());
            page.setText(list.get(position).getMessengTitle());

            linearLayout.setTag(position);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag();
                    Intent intent = new Intent();
                    intent.setClass(MessengAll.this,MessengPage.class);
                    intent.putExtra("messeng",list.get(position));
                    startActivity(intent);
                }
            });

            return v;
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123){
                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                JsonArray jsonArray = parser.parse(str).getAsJsonArray();
                for (JsonElement json : jsonArray) {
                    list.add(gson.fromJson(json , Messeng.class));
                }
                listView.setAdapter(adapter);
            }else if (msg.what == 0x000){
                Snackbar.make(listView, "网络连接失败，请检查您的网络", Snackbar.LENGTH_LONG).show();
            }
        }
    };
}
