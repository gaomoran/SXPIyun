package com.mrg.sxpiyun.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zxg.sxpi.ui.R;
import com.mrg.sxpiyun.util.AppConfig;
import com.mrg.sxpiyun.util.ConnectionAdmin;
import com.mrg.sxpiyun.util.Connent;
import com.mrg.sxpiyun.util.FriendInfo;
import com.mrg.sxpiyun.util.IsLogin;
import com.mrg.sxpiyun.util.UserAdmin;

import java.io.File;

public class TestLoginActivity extends Activity {
    private final int REGLOGTRUE = 1;
    private final int LOGINTRUE=2;
    private final int CONNENTFLAS=3;
    private final int LOGTINFLAS=4;
    private final int WEIZHICUOWU=5;
    private EditText ui_username_input;
    private EditText ui_password_input;
    private ProgressBar Loading;
    private AppConfig appConfig;
    private String username;
    private String password;
    private File file;
    private String nichen;
    private String email;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case LOGINTRUE://连接正常，登陆正常
                    ConnectionAdmin.servicename = ConnectionAdmin.conns.getServiceName();
                    Intent intent = new Intent(TestLoginActivity.this, MainActivity.class);
                    startActivity(intent);

                    finish();
                    break;
                case CONNENTFLAS://打开连接失败
                    Toast.makeText(getApplicationContext(), "与服务器连失败", Toast.LENGTH_LONG).show();
                    break;
                case LOGTINFLAS://登陆失败
                    Toast.makeText(getApplicationContext(), "用户名或密码错误", Toast.LENGTH_LONG).show();
                    break;
                case WEIZHICUOWU:
                    Toast.makeText(getApplicationContext(), "未知错误", Toast.LENGTH_LONG).show();
                    break;

            }
        }
    };
    private FriendInfo friendInfo;
    private Runnable sendable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_login);
        ui_username_input = (EditText) findViewById(R.id.dt_username);
        ui_password_input = (EditText) findViewById(R.id.dt_password);



        Loading = (ProgressBar) findViewById(R.id.testlogin_loading);
        file = new File("/data/data/com.zxg.sxpi.yun/shared_prefs", "Appconfig.xml");
        TextView textView = (TextView) findViewById(R.id.tv_top_title);
        textView.setText("用户登陆");
        sendable = new Runnable() {

            @Override
            public void run() {
                Connent connent = new Connent();
                if (!connent.onConnet()) {
                    Message message = handler.obtainMessage();
                    message.what = CONNENTFLAS;
                    handler.sendMessage(message);
                } else {
                    if (!connent.onLogin(username, password)) {

                        Message message = handler.obtainMessage();
                        message.what = LOGTINFLAS;
                        handler.sendMessage(message);
                    } else {
                        if (file.exists()) {
                            //是否首次加载
                            friendInfo = new FriendInfo();
                            String tureName = friendInfo.getTureName();
                            String tureClass = friendInfo.getTureClass();
                            String professional = friendInfo.getProfessional();
                            UserAdmin.classs = tureClass;
                            UserAdmin.truename = tureName;
                            UserAdmin.professional = professional;
                            AppConfig appConfig = new AppConfig(getApplication());
                            appConfig.addUserConfig(username, password, nichen);
                            appConfig.setTureInfo(tureName, tureClass, professional);


                        }
                        Message message = handler.obtainMessage();
                        message.what = LOGINTRUE;
                        handler.sendMessage(message);
                        //修改配置文件
                        IsLogin.islogin = true;

                    }
                }

            }

        };

    }

    /**
     * 登陆处理事件
     */
    public void Login_OnClick(View v) {


        username = ui_username_input.getText().toString().trim();
        password = ui_password_input.getText().toString().trim();
        appConfig = new AppConfig(getApplicationContext());


        new Thread(sendable).start();

    }

    /**
     * 注册
     *
     * @param v
     */
    public void login_logreg(View v) {
        Intent intent = new Intent(TestLoginActivity.this, LogregActivity.class);
        startActivityForResult(intent, REGLOGTRUE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REGLOGTRUE && resultCode == REGLOGTRUE) {
            username = data.getStringExtra("username");
            password = data.getStringExtra("password");
            nichen=data.getStringExtra("nichen");
            email = data.getStringExtra("email");
            new Thread(sendable).start();

        }
    }

    public void top_backward(View v) {
        finish();
    }

}
