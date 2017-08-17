package com.example.tong.androidtour;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.tong.androidtour.Bean.Messeng;
import com.example.tong.androidtour.Fragment.Fragment1;
import com.example.tong.androidtour.Util.App;
import com.example.tong.androidtour.Util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tong on 17-3-28.
 */

public class MessengPage extends AppCompatActivity {
    private ImageView iv;
    private TextView nm,nt,name,page,room,address;
    private LinearLayout layout;
    private Button yd,qxyd;
    private Messeng messeng;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messeng);

        iv = (ImageView) findViewById(R.id.messeng_img);
        nm = (TextView) findViewById(R.id.messeng_nm);
        nt = (TextView) findViewById(R.id.messeng_nt);
        name = (TextView) findViewById(R.id.messeng_name);
        page = (TextView) findViewById(R.id.messeng_page);
        layout = (LinearLayout) findViewById(R.id.messeng_hotel);
        room = (TextView) findViewById(R.id.messeng_room);
        yd = (Button) findViewById(R.id.messeng_yd);
        qxyd = (Button) findViewById(R.id.messeng_qxyd);
        address = (TextView) findViewById(R.id.messeng_address);

        String type = null;

        int roomType = App.user.getRoomType();

        messeng = (Messeng) getIntent().getSerializableExtra("messeng");
        switch (messeng.getMessengType()){
            case 0:
                iv.setBackgroundResource(R.drawable.mes);
                type = "景点";
                layout.setVisibility(View.GONE);
                break;
            case 1:
                iv.setBackgroundResource(R.drawable.hotel);
                room.setText(messeng.getMessengRoom()+"");
                type = "酒店";
                if (roomType == 1){
                    yd.setVisibility(View.GONE);
                    qxyd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new Thread(){
                                @Override
                                public void run() {
                                    super.run();
                                    Map<String,Object> map =new HashMap<>();
                                    map.put("messengId",messeng.getMessengId());
                                    String str = HttpUtil.doPost(HttpUtil.path+"CancelHotelServlet",map);
                                    if (str.equals("error")) {
                                        handler.sendEmptyMessage(0x000);
                                    }else if (str.equals("true")){
                                        handler.sendEmptyMessage(0x223);
                                    }else{
                                        handler.sendEmptyMessage(0x224);
                                    }
                                }
                            }.start();
                        }
                    });
                }else {
                    qxyd.setVisibility(View.GONE);
                    yd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new Thread(){
                                @Override
                                public void run() {
                                    super.run();
                                    Map<String,Object> map =new HashMap<>();
                                    map.put("messengId",messeng.getMessengId());
                                    String str = HttpUtil.doPost(HttpUtil.path+"UpdateHotelServlet",map);
                                    if (str.equals("error")) {
                                        handler.sendEmptyMessage(0x000);
                                    }else if (str.equals("true")){
                                        handler.sendEmptyMessage(0x123);
                                    }else{
                                        handler.sendEmptyMessage(0x124);
                                    }
                                }
                            }.start();
                        }
                    });
                }
                break;
            case 2:
                iv.setBackgroundResource(R.drawable.foot);
                type = "美食";
                layout.setVisibility(View.GONE);
                break;
        }
        nm.setText(type+"名");
        nt.setText(type+"介绍");

        name.setText(messeng.getMessengName());
        page.setText(messeng.getMessengTitle()+"\n\n"+messeng.getMessengPage());
        address.setText(messeng.getMessengAddress());

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x123:
                    Snackbar.make(qxyd, "酒店预定成功", Snackbar.LENGTH_LONG).show();
                    App.user.setRoomType(1);
                    yd.setVisibility(View.GONE);
                    qxyd.setVisibility(View.VISIBLE);
                    break;
                case 0x124:
                    Snackbar.make(qxyd, "酒店预定失败", Snackbar.LENGTH_LONG).show();
                    break;
                case 0x223:
                    Snackbar.make(qxyd, "取消预定成功", Snackbar.LENGTH_LONG).show();
                    App.user.setRoomType(0);
                    qxyd.setVisibility(View.GONE);
                    yd.setVisibility(View.VISIBLE);
                    break;
                case 0x224:
                    Snackbar.make(qxyd, "取消预定失败", Snackbar.LENGTH_LONG).show();
                    break;
                case 0x000:
                    Snackbar.make(qxyd, "网络连接失败，请检查您的网络", Snackbar.LENGTH_LONG).show();
                    break;
            }
        }
    };
}
