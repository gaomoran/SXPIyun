package com.mrg.sxpiyun.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zxg.sxpi.ui.R;
import com.mrg.sxpiyun.modle.Results;
import com.mrg.sxpiyun.modle.Schedle;
import com.mrg.sxpiyun.util.AppConfig;
import com.mrg.sxpiyun.util.FriendInfo;
import com.mrg.sxpiyun.util.HttpHelper;
import com.mrg.sxpiyun.util.UserAdmin;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JiaoWuLoginActivity extends AppCompatActivity {
    private final int UPDATAFLAS=234;
    private final int UPDATA_SCHEDULE_UI=1;
    private final int UPDATA_RESUTLS_UI=3;

    private String username;
    private String pwd;
    private final int LOGIN_X=2;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {

                case UPDATA_SCHEDULE_UI:
                    Toast.makeText(getApplication(),"登陆成功",Toast.LENGTH_SHORT).show();
                    List<Schedle> document = (List<Schedle>) msg.obj;

                    Intent intent = new Intent();
                    intent.putExtra("schedle", (Serializable) document);
                    setResult(2, intent);
                    finish();
                    break;
                case LOGIN_X:
                    Toast.makeText(getApplication(),"登陆失败",Toast.LENGTH_SHORT).show();
                    break;

                case UPDATAFLAS:
                    Toast.makeText(getApplication(),"上传失败",Toast.LENGTH_SHORT).show();
                    break;
                case UPDATA_RESUTLS_UI:
                    List<Results> doc=(List<Results>)msg.obj;
                    Intent intent2 = new Intent();
                    intent2.putExtra("results", (Serializable) doc);
                    setResult(UPDATA_RESUTLS_UI, intent2);
                    finish();
                    Toast.makeText(getApplication(),"登陆成功",Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };
    private EditText dt_username;
    private EditText dt_password;
    private AppConfig appConfig;
    private FriendInfo friendInfo;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jiao_wu_login);

        dt_username = (EditText) findViewById(R.id.dt_jaowulogin_username);
        dt_password = (EditText) findViewById(R.id.dt_jaowulogin_password);
        friendInfo = new FriendInfo();

        appConfig = new AppConfig(getApplicationContext());
        TextView textView = (TextView)findViewById(R.id.tv_top_title);
        textView.setText("教务系统登陆");
        intent = getIntent();


    }


    public void Login_OnClick(View v){

        username = dt_username.getText().toString();
        pwd = dt_password.getText().toString();
        new Thread() {
            public void run() {
                try {
                    HttpHelper httpHelper = new HttpHelper();
                    Map<String, String> cookie = httpHelper.loginCon(username, pwd);
                    if (cookie != null) {
                        //网络获取课表
                        Document document = httpHelper.getSchedule(cookie, username);
                        List<Schedle> schedule = httpHelper.getSchedule(document);
                        //网络获取成绩单
                        Document results = httpHelper.selectrseults(cookie, username);
                        List<Results> resultses=httpHelper.getResults(results);
                        //网络获取真实个人信息
                        HashMap<String, String> tureInfo = httpHelper.getTureInfo(cookie);

                        String name = tureInfo.get("name");
                        if (name!=null){
                            UserAdmin.truename= name;
                        }else {
                            UserAdmin.truename="";
                        }

                        String aClass = tureInfo.get("class");
                        if (aClass!=null){
                            UserAdmin.classs= aClass;
                        }else {
                            UserAdmin.classs="";
                        }

                        String professional = tureInfo.get("professional");
                        if (professional!=null){
                            UserAdmin.professional= professional;
                        }else {
                            UserAdmin.professional="";
                        }
                        //名字班级记录到配置文件
                        appConfig.setTureInfo(UserAdmin.truename, UserAdmin.classs, UserAdmin.professional);
                        //上传名字和班级
                        if(!friendInfo.setTureName(name)&&!friendInfo.setTureClass(aClass)){
                            Message message = handler.obtainMessage();
                            message.what=UPDATAFLAS;
                            handler.sendMessage(message);
                        }

                        if (intent.getStringExtra("type").equals("课表")){
                            Message message = handler.obtainMessage();
                            message.obj=resultses;
                            message.what = UPDATA_RESUTLS_UI;
                            handler.sendMessage(message);
                        }else {

                            Message message = handler.obtainMessage();
                            message.obj=schedule;
                            message.what = UPDATA_SCHEDULE_UI;
                            handler.sendMessage(message);
                        }
                    }else {
                        Message message = handler.obtainMessage();
                        message.what = LOGIN_X;
                        handler.sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Message message = handler.obtainMessage();
                    message.what = LOGIN_X;
                    handler.sendMessage(message);
                }
            }
        }.start();




    }

    public void top_backward(View v){
        finish();

    }
}
