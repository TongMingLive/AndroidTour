package com.example.tong.androidtour;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tong.androidtour.Util.App;
import com.example.tong.androidtour.Util.HttpUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tong on 2017/3/19.
 */

public class UpdatePassword extends AppCompatActivity {
    private TextView oldPassword, password1, password2;
    private Button button;
    private String old, new1, new2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_password);
        oldPassword = (TextView) findViewById(R.id.update_password_old);
        password1 = (TextView) findViewById(R.id.update_password1);
        password2 = (TextView) findViewById(R.id.update_password2);
        button = (Button) findViewById(R.id.update_btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                old = oldPassword.getText().toString();
                new1 = password1.getText().toString();
                new2 = password2.getText().toString();
                if (!App.user.getUserPassword().equals(old)) {
                    oldPassword.setError("原密码输入错误");
                } else if (!new1.equals(new2)) {
                    password2.setError("两次密码输入不一致");
                } else {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            Map<String, Object> map = new HashMap<>();
                            map.put("userId", App.user.getUserId());
                            map.put("userPassword", new1);
                            String str = HttpUtil.doPost(HttpUtil.path + "UpdateUserPasswordServlet", map);
                            if ("error".equals(str)) {
                                Log.e("login", "isNot");
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
                App.user.setUserPassword(new1);
                Toast.makeText(UpdatePassword.this, "修改成功", Toast.LENGTH_SHORT).show();
                finish();
            } else if (msg.what == 0x124) {
                Snackbar.make(button, "修改失败，请重试", Snackbar.LENGTH_LONG).show();
            } else if (msg.what == 0x000) {
                Snackbar.make(button, "网络连接失败，请检查您的网络", Snackbar.LENGTH_LONG).show();
            }
        }
    };
}
