package com.example.tong.androidtour;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tong.androidtour.Util.App;
import com.example.tong.androidtour.Util.HttpUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tong on 17-3-30.
 */

public class FriendPush extends AppCompatActivity {
    private EditText title,page;
    private Button btn;
    private String strTitle,strPage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_push);
        title = (EditText) findViewById(R.id.friend_push_title);
        page = (EditText) findViewById(R.id.friend_push_page);
        btn = (Button) findViewById(R.id.friend_push_btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strTitle = title.getText().toString();
                strPage = page.getText().toString();
                if (TextUtils.isEmpty(strTitle)){
                    title.setError("请输入标题");
                }else if (TextUtils.isEmpty(strPage)){
                    title.setError("请输入内容");
                }else {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            Map<String, Object> map = new HashMap<>();
                            map.put("userId", App.user.getUserId());
                            map.put("friendTitle", strTitle);
                            map.put("friendPage", strPage);
                            String str = HttpUtil.doPost(HttpUtil.path + "InsertFriendServlet", map);
                            if ("error".equals(str)) {
                                handler.sendEmptyMessage(0x000);
                            } else if ("true".equals(str)) {
                                handler.sendEmptyMessage(0x123);
                            } else {
                                handler.sendEmptyMessage(0x124);
                            }
                        }
                    }.start();
                }
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123) {
                Toast.makeText(FriendPush.this, "发布成功", Toast.LENGTH_SHORT).show();
                finish();
            } else if (msg.what == 0x124) {
                Snackbar.make(btn, "发布失败，请重试", Snackbar.LENGTH_LONG).show();
            } else if (msg.what == 0x000) {
                Snackbar.make(btn, "网络连接失败，请检查您的网络", Snackbar.LENGTH_LONG).show();
            }
        }
    };
}
