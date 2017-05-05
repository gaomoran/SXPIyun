package com.mrg.sxpiyun.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mrg.sxpiyun.modle.ZuiXinXiaoXi;
import com.mrg.sxpiyun.util.GetName;

import org.jivesoftware.smack.packet.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrG on 2016-10-14.
 */
public class XiaoXiListBD {

    MyDatebase myDatebase;
    private final LianXiRenBD lianXiRenBD;

    public XiaoXiListBD(Context context,String useruame){
        myDatebase=new MyDatebase(context,useruame);
        lianXiRenBD = new LianXiRenBD(context);
    }

    /**
     * 添加新的通讯消息
     */
    public void addNewChatMsg(Message message,int shu,long systime){
        SQLiteDatabase db = myDatebase.getWritableDatabase();
        ContentValues values = new ContentValues();

        //用户名
        values.put("username", GetName.getName(message.getFrom().toString()));
        //消息数
        values.put("msgshu",shu);
        //时间
        values.put("data",systime);
        //消息内容
        values.put("newmsg",message.getBody().toString());
        //标题
        values.put("title", lianXiRenBD.getNichen(message.getFrom()));
        db.insert("xiaoxilist", null, values);
        db.close();
    }

    /**
     *添加一条其他类型的消息
     * @param message
     * @param title
     */
    public void addQiTaMsg(Message message,String title){
        SQLiteDatabase db = myDatebase.getWritableDatabase();
        ContentValues values = new ContentValues();

        //用户名
        values.put("username", GetName.getName(message.getFrom().toString())+"[#"+title+"#]");
        //消息数
        values.put("msgshu","1");
        //时间
        values.put("data",System.currentTimeMillis());
        //消息内容
        values.put("newmsg",message.getBody().toString());
        //标题
        values.put("title", title);
        db.insert("xiaoxilist", null, values);
        db.close();
    }
    /**
     * 移除一条消息
     */
    public void removeXiaoxi(String username){

        SQLiteDatabase db = myDatebase.getWritableDatabase();
        db.delete("xiaoxilist", "username=?", new String[]{username});
        db.close();
    }
    /**
     * 查询所有消息
     */
    public List<ZuiXinXiaoXi> quertAllXiaoXi(){
        List<ZuiXinXiaoXi> xiaoxi=new ArrayList<ZuiXinXiaoXi>();

        try {
            SQLiteDatabase db = myDatebase.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from xiaoxilist order by data DESC", new String[]{});
            if (cursor!=null) {
                while (cursor.moveToNext()) {
                    ZuiXinXiaoXi zxxx = new ZuiXinXiaoXi();
                    zxxx.setName(cursor.getString(0));
                    zxxx.setNews(cursor.getString(3));
                    zxxx.setData(cursor.getLong(2));
                    zxxx.setMsgshu(cursor.getInt(1));
                    zxxx.setTitle(cursor.getString(4));
                    xiaoxi.add(zxxx);
                    System.out.println("0:" + cursor.getString(0) + "1:" + cursor.getString(1) + "2:" + cursor.getString(2) + "3:" + cursor.getString(3) + "4:" + cursor.getString(4));

                }

            }
            db.close();


        }catch (Exception e){
            e.printStackTrace();
        }


        return xiaoxi;

    }
    /**
     * 查询是否已经有了该用户的消息
     */
    public boolean quertIsExist(String username){
        SQLiteDatabase db = myDatebase.getWritableDatabase();
        try {
            Cursor cursor= db.rawQuery("select * from xiaoxilist where username=?", new String[]{username});
            if (cursor!=null) {
                db.close();
                return true;
            }else {
                db.close();
                return false;
            }
        }catch (Exception e){
            db.close();
            return false;
        }


    }
    /**
     * 根据用户名查询该用户有几条最新消息
     */
    public int quertNewMsgShu(String username){
        SQLiteDatabase db = myDatebase.getWritableDatabase();
        Cursor cursor= db.rawQuery("select msgshu from xiaoxilist where username=?", new String[]{username});
        int msgshu=0;
        if (cursor!=null) {
            while (cursor.moveToNext()) {

                msgshu=cursor.getInt(0);

            }

        }
        db.close();
        return msgshu;
    }
    /**
     * 根据用户名修改最新消息数
     */
    public void updateNewMsgshu(String username,int shu){
        SQLiteDatabase db = myDatebase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("msgshu",shu);

        db.update("xiaoxilist", values, "username=?", new String[]{username});
        db.close();
    }




}
