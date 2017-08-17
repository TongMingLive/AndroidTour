package com.example.msit;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

public class RegeistActivity extends AppCompatActivity{
    private EditText regUsername,regPass,regTel,name;
    private Button btn;
    private RadioGroup rg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regeist);
        regUsername = (EditText) findViewById(R.id.reg_username);
        regPass = (EditText) findViewById(R.id.reg_pass);
        regTel = (EditText) findViewById(R.id.reg_tel);
        name = (EditText) findViewById(R.id.reg_idcard);
        btn = (Button) findViewById(R.id.reg_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(regUsername.getText())){
                    Toast.makeText(RegeistActivity.this,"用户名不能为空",Toast.LENGTH_LONG).show();
                    return;
                }
                else if(TextUtils.isEmpty(regPass.getText())){
                    Toast.makeText(RegeistActivity.this,"密码不能为空",Toast.LENGTH_LONG).show();
                    return;
                }
                else if(TextUtils.isEmpty(name.getText())){
                    T.show("昵称不能为空");
                    return;
                }
                else if(TextUtils.isEmpty(regTel.getText())){
                    T.show("电话号码不能为空");
                    return;
                }
                else{
                    Request<String> request = NoHttp.createStringRequest(Constant.regUrl, RequestMethod.POST);
                    request.add("username",regUsername.getText().toString().trim());
                    request.add("password",regPass.getText().toString().trim());
                    request.add("tel",regTel.getText().toString().trim());
                    request.add("name",name.getText().toString().trim());
                    request.add("pic","");
                    Constant.getQueueInstance().add(0x122, request, listener);
                }
            }
        });
    }


    private OnResponseListener<String> listener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {

        }
        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == 0x122){
                if("true".equals(response.get())){
                    T.show("注册成功");
                    finish();
                }
                else if("false".equals(response.get())){
                    T.show("注册失败");
                }
            }
        }

        @Override
        public void onFailed(int what, Response<String> response) {

        }
        @Override
        public void onFinish(int what) {

        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
