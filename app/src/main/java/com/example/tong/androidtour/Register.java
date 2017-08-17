package com.example.tong.androidtour;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tong.androidtour.Util.HttpUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tong on 2016/12/7.
 */

public class Register extends AppCompatActivity {
    private EditText userName, userPsw_1, userPsw_2;
    private Button register;
    private String name, psw;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        userName = (EditText) findViewById(R.id.register_username);
        userPsw_1 = (EditText) findViewById(R.id.register_password_1);
        userPsw_2 = (EditText) findViewById(R.id.register_password_2);
        register = (Button) findViewById(R.id.register_log);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = userName.getText().toString();
                psw = userPsw_1.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    userName.setError("请输入账号");
                } else if (TextUtils.isEmpty(psw)) {
                    userPsw_1.setError("请输入密码");
                } else if (TextUtils.isEmpty(userPsw_2.getText().toString())) {
                    userPsw_2.setError("请再次输入密码");
                } else if (!psw.equals(userPsw_2.getText().toString())) {
                    userPsw_2.setError("两次密码输入不一致");
                } else {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("userName", name);
                            map.put("userPassword", psw);
                            String str = HttpUtil.doPost(HttpUtil.path + "SelectUserNameServlet", map);
                            Log.e("register", str);
                            if ("error".equals(str)) {
                                handler.sendEmptyMessage(0x000);
                            } else if ("false".equals(str)) {
                                handler.sendEmptyMessage(0x001);
                            } else {
                                String registerStr = HttpUtil.doPost(HttpUtil.path + "UserRegisterServlet", map);
                                if ("true".equals(registerStr)) {
                                    handler.sendEmptyMessage(0x123);
                                } else {
                                    handler.sendEmptyMessage(0x124);
                                }
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
                Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Register.this, Login.class));
                finish();
            } else if (msg.what == 0x124) {
                Snackbar.make(register, "注册失败，请重试", Snackbar.LENGTH_LONG).show();
            } else if (msg.what == 0x001) {
                Snackbar.make(register, "用户名重复，请重新输入", Snackbar.LENGTH_LONG).show();
            } else if (msg.what == 0x000) {
                Snackbar.make(register, "网络连接失败，请检查您的网络", Snackbar.LENGTH_LONG).show();
            }
        }
    };
}
