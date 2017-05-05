package com.mrg.sxpiyun.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;

/**
 * Created by MrG on 2016-10-20.
 */
public class AppConfig{


    private Context context;

    public AppConfig(Context context){

        this.context=context;

    }

    /**
     * 将用户名和密码保存到配置文件中
     * @param username
     * @param password
     */
    public void addUserConfig(String username,String password,String nichen) {
        File file= new File("/data/data/com.zxg.sxpi.yun/shared_prefs", "Appconfig.xml");
        if (file.exists()) {
            System.out.println("配置文件中保存用户名密码");
            SharedPreferences share =context.getSharedPreferences("Appconfig", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = share.edit();//获取编辑器
            editor.putString("username", username);
            editor.putString("password", password);
            editor.putString("nichen", nichen);
            editor.putString("servicename", ConnectionAdmin.conns.getServiceName());
            editor.commit();

        }
    }
    public void setTureInfo(String name,String classs,String professional){
        File file= new File("/data/data/com.zxg.sxpi.yun/shared_prefs", "Appconfig.xml");
        if (file.exists()) {
            System.out.println("配置文件中保存正是姓名和班级");
            SharedPreferences share =context.getSharedPreferences("Appconfig", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = share.edit();//获取编辑器
            editor.putString("name", name);
            editor.putString("class", classs);
            editor.putString("professional", professional);

            editor.commit();

        }else{
            System.out.println("配置文件不存在");
        }


    }
    public void Closelogin(){
        File file= new File("/data/data/com.zxg.sxpi.yun/shared_prefs", "Appconfig.xml");
        if (file.exists()) {
            System.out.println("清除所有信息");
            SharedPreferences share =context.getSharedPreferences("Appconfig", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = share.edit();//获取编辑器

            editor.putString("name", "");
            editor.putString("username", "");
            editor.putString("password", "");
            editor.putString("class", "");
            editor.putString("professional", "");

            editor.commit();

        }else{
            System.out.println("配置文件不存在");
        }




    }
}
