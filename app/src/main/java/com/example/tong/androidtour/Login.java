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
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.tong.androidtour.Bean.User;
import com.example.tong.androidtour.Util.App;
import com.example.tong.androidtour.Util.HttpUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tong on 2017/3/14.
 */

public class Login extends AppCompatActivity {
    private long exitTime = 0;
    private EditText userName, userPsw;
    private Button login, regist;
    private String name, psw;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        userName = (EditText) findViewById(R.id.login_username);
        userPsw = (EditText) findViewById(R.id.login_password);
        login = (Button) findViewById(R.id.login_log);
        regist = (Button) findViewById(R.id.login_regist);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = userName.getText().toString();
                psw = userPsw.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    userName.setError("请输入账号");
                } else if (TextUtils.isEmpty(psw)) {
                    userPsw.setError("请输入密码");
                } else {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("userName", name);
                            map.put("userPassword", psw);
                            String str = HttpUtil.doPost(HttpUtil.path + "AndroidLoginServlet", map);
                            Log.d("Login", str);
                            if ("error".equals(str)) {
                                Log.d("login", "isNot");
                                handler.sendEmptyMessage(0x000);
                            } else {
                                Log.d("login", "isOk");
                                Gson gson = new Gson();
                                App.user = gson.fromJson(str, User.class);
                                Log.d("userId", App.user.getUserId() + "");
                                if (App.user.getUserId() > 0) {
                                    if (App.user.getUserType() == 1) {
                                        handler.sendEmptyMessage(0x125);
                                    } else {
                                        handler.sendEmptyMessage(0x123);
                                    }
                                } else {
                                    handler.sendEmptyMessage(0x124);
                                }
                            }
                        }
                    }.start();
                }
            }
        });

        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123) {
                finish();
                startActivity(new Intent(Login.this, MainActivity.class));
            } else if (msg.what == 0x124) {
                Snackbar.make(login, "帐号或密码错误", Snackbar.LENGTH_LONG).show();
            } else if (msg.what == 0x125) {
                Snackbar.make(login, "管理员请在jsp端登陆后台", Snackbar.LENGTH_LONG).show();
            } else if (msg.what == 0x000) {
                Snackbar.make(login, "网络连接失败，请检查您的网络", Snackbar.LENGTH_LONG).show();
            }
        }
    };

    //再次返回键退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Snackbar.make(login, "再按一次退出程序", Snackbar.LENGTH_SHORT).show();
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
