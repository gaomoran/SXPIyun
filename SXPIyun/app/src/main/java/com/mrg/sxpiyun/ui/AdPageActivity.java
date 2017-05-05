package com.mrg.sxpiyun.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import com.zxg.sxpi.ui.R;
import com.mrg.sxpiyun.util.ConnectionAdmin;
import com.mrg.sxpiyun.util.UserAdmin;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class AdPageActivity extends Activity {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_page);
        //加载配置文件
        File file= new File("/data/data/"+getPackageName().toString()+"/shared_prefs","Appconfig.xml");
        Timer timer = new Timer();
        TimerTask login = new TimerTask(){

            public void run(){

                Intent intent=new Intent(getApplicationContext(),TestLoginActivity.class);
                startActivity(intent);
                finish();
            }

        };

        TimerTask main = new TimerTask(){

            public void run(){

                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }

        };


        if(file.exists()) {
            System.out.println("存在配置文件");
            SharedPreferences share=getSharedPreferences("Appconfig",Activity.MODE_PRIVATE);

            UserAdmin.username=share.getString("username","");
            UserAdmin.password=share.getString("password","");
            UserAdmin.truename=share.getString("name","");
            UserAdmin.classs=share.getString("class","");
            UserAdmin.nichen=share.getString("nichen","");
            UserAdmin.professional=share.getString("professional","");
            ConnectionAdmin.servicename=share.getString("servicename","");
            System.out.println("用户名" + UserAdmin.username + "===密码" + UserAdmin.password);
            if (UserAdmin.username.equals("")||UserAdmin.password.equals("")){
                timer.schedule(login,3000);
            }else {
                timer.schedule(main, 3000);
            }
        }else {//文件不存在则创建
            System.out.println("不存在配置文件");
            SharedPreferences sharedPreferences = getSharedPreferences("Appconfig", Context.MODE_PRIVATE); //私有数据
            SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
            //是否首次加载
            editor.putString("username", "");
            editor.putString("password", "");
            editor.putString("name","");
            editor.putString("class","");
            editor.putString("nichen","");
            editor.putString("professional","");
            editor.putString("servicename",null);
            editor.commit();

            timer.schedule(login, 3000);
        }
  











    }
}
