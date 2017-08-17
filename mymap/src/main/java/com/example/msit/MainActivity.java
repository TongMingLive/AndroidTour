package com.example.msit;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button loginBtn,regBtn;
    private EditText name,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initState();
        loginBtn = (Button) findViewById(R.id.login_btn);
        regBtn = (Button) findViewById(R.id.regeist_btn);
        name = (EditText) findViewById(R.id.login_username);
        pass = (EditText) findViewById(R.id.login_password);
        loginBtn.setOnClickListener(this);
        regBtn.setOnClickListener(this);
    }
    private void initState() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn:
                if(TextUtils.isEmpty(name.getText())){
                    Toast.makeText(this,"用户名不能为空",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(pass.getText())){
                    Toast.makeText(this,"密码不能为空",Toast.LENGTH_LONG).show();
                }
                else{
                    final Request<User> request = new JavaBeanRequest<User>(Constant.loginUrl, RequestMethod.GET,User.class);
                    request.add("username",name.getText().toString().trim());
                    request.add("password",pass.getText().toString().trim());
                    Constant.getQueueInstance().add(0x111, request, new OnResponseListener<User>() {
                        @Override
                        public void onStart(int what) {
                        }

                        @Override
                        public void onSucceed(int what, Response<User> response) {
                            if(response.get()!=null){
                                T.show("登陆成功");
                                Constant.user = response.get();
                                startActivity(new Intent(MainActivity.this,IndexActivity.class));
                                finish();
                            }
                            else{
                                T.show("用户名或密码错误");
                            }
                        }

                        @Override
                        public void onFailed(int what, Response<User> response) {
                            T.show("请检查网络");
                        }

                        @Override
                        public void onFinish(int what) {

                        }
                    });
                }

                break;
            case R.id.regeist_btn:
                startActivity(new Intent(MainActivity.this,RegeistActivity.class));
                // overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                overridePendingTransition(R.anim.right_in,R.anim.left_out);
                break;
        }
    }
}
