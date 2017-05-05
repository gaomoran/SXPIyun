package com.mrg.sxpiyun.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mrg.sxpiyun.util.GetName;

import org.jivesoftware.smack.packet.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrG on 2016-07-27.
 */
public class XiaoXiBD {
    MyDatebase myDatebase;
    public XiaoXiBD(Context context,String useruame){
        myDatebase=new MyDatebase(context,useruame);
    }



    /**
     * 添加一条新消息
     * @param message
     */
    public void addNewMsg(Message message){
        SQLiteDatabase db = myDatebase.getWritableDatabase();
        ContentValues values = new ContentValues();
        //获取系统当前时间
        long systime = System.currentTimeMillis();
        values.put("fromname", GetName.getName(message.getFrom()));
        values.put("toname",GetName.getName(message.getTo()));
        values.put("body",message.getBody());
        values.put("data",systime);
        values.put("isnew","yes");
        values.put("type","chat");
        db.insert("xiaoxi",null,values);
        db.close();

    }

    /**
     * 添加一条消息，附带是否是新消息
     * @param message
     * @param isNew
     */
    public void addNewMsg(Message message,String isNew){
        SQLiteDatabase db = myDatebase.getWritableDatabase();
        ContentValues values = new ContentValues();

        //获取系统当前时间
        long systime = System.currentTimeMillis();
        values.put("fromname", GetName.getName(message.getFrom()));
        values.put("toname",GetName.getName(message.getTo()));
        values.put("body",message.getBody());
        values.put("data",systime);
        values.put("isnew",isNew);
        values.put("type","chat");
        db.insert("xiaoxi", null, values);
        db.close();

    }

    /**
     * 添加一条请假消息
     * @param message
     */
    public void addLeaveMsg(Message message){
        SQLiteDatabase db = myDatebase.getWritableDatabase();
        ContentValues values = new ContentValues();
        //获取系统当前时间
        long systime = System.currentTimeMillis();
        values.put("fromname", GetName.getName(message.getFrom()));
        values.put("toname",GetName.getName(message.getTo()));
        values.put("body",message.getBody());
        values.put("data",systime);
        values.put("isnew","yes");
        values.put("type","leave");
        db.insert("xiaoxi", null, values);
        db.close();

    }

    /**
     * 查询未读消息
     */

    public List<Message> getWeiduMsg(String username){
        List<Message> list = new ArrayList<Message>();
        SQLiteDatabase db = myDatebase.getReadableDatabase();

        Cursor cursor= db.rawQuery("select fromname,toname,body from xiaoxi where type=? and (fromname=? or toname=?) order by data", new String[]{"chat",username,username});
        while (cursor.moveToNext()){
            Message message = new Message();
            message.setFrom(cursor.getString(0));
            message.setTo(cursor.getString(1));
            message.setBody(cursor.getString(2));
            list.add(message);

        }
        db.close();
        return list;
    }

    /**
     * 查询所有请假消息
     * @return
     */
    public List<Message> getLeaveMsgAll(String username){
        List<Message> list = new ArrayList<Message>();
        SQLiteDatabase db = myDatebase.getReadableDatabase();

        Cursor cursor= db.rawQuery("select fromname,toname,body from xiaoxi where fromname=? and type=? order by data DESC", new String[]{GetName.getName(username),"leave"});
        while (cursor.moveToNext()){
            Message message = new Message();
            message.setFrom(cursor.getString(0));
            message.setTo(cursor.getString(1));
            message.setBody(cursor.getString(2));
            list.add(message);

        }
        db.close();
        return list;
    }

    /**
     * 移除一条请假消息
     * @param username
     */
    public void removeLeaveMsg(String username){
        SQLiteDatabase db = myDatebase.getWritableDatabase();
        db.delete("xiaoxi","fromname=? and type=?",new String[]{GetName.getName(username),"leave"});
        db.close();
    }


}
