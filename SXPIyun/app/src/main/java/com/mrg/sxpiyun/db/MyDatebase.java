package com.mrg.sxpiyun.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by MrG on 2016-07-26.
 */
public class MyDatebase extends SQLiteOpenHelper {
    public MyDatebase(Context context,String username) {
        super(context,username+".db",null,2);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //联系人表，字段包含（用户名（主键），组名，昵称，状态,头像地址,权限）
        db.execSQL("CREATE TABLE IF NOT EXISTS lianxiren(username varchar(25) constraint pk primary key,groupname varchar(50),niche varchar(20),zhuangtai varchar(20),touxiang varchar(100),permission varchar(5))");
        //消息表，字段包含（id（主键自动添加），来自谁，发给谁，消息内容，时间,是否为新消息）
        db.execSQL("CREATE TABLE IF NOT EXISTS xiaoxi(id integer primary key autoincrement,fromname varchar(25),toname varchar(25),body varchar(255),data integer,isnew varchar(5),type varchar(20))");
        //消息列表表，字段包含（用户名（主键），新消息条数，时间，最新消息）
        db.execSQL("CREATE TABLE IF NOT EXISTS xiaoxilist(username varchar(25) primary key,msgshu int,data integer,newmsg varchar(255),title varchar(25))");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
