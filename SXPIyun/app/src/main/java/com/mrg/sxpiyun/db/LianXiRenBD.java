package com.mrg.sxpiyun.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mrg.sxpiyun.modle.LianXiRenMod;
import com.mrg.sxpiyun.modle.QueryHaoyouMod;
import com.mrg.sxpiyun.util.FriendInfo;
import com.mrg.sxpiyun.util.GetName;
import com.mrg.sxpiyun.util.LianxirenList;
import com.mrg.sxpiyun.util.UserAdmin;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MrG on 2016-07-26.
 */
public class LianXiRenBD {
    private MyDatebase myDatebase;
    private FriendInfo friendInfo=new FriendInfo();

    public LianXiRenBD(Context context){
        myDatebase = new MyDatebase(context, UserAdmin.username);
    }


    /**
     * 查询全部组名
     *
     * @return
     */
    public List<String> getLianxirenGroup(){
        List<String> list=new ArrayList<String>();
        SQLiteDatabase db = myDatebase.getReadableDatabase();
        Cursor cursor= db.rawQuery("select distinct groupname from lianxiren", new String[]{});
        while(cursor.moveToNext()){
            String groupname = cursor.getString(0);
            list.add(groupname);
        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     * 根据组名查询组内详细成员信息
     *
     * @param groupname
     * @return
     */
    public List<LianXiRenMod> getLianxiren(String groupname){
        List<LianXiRenMod> list=new ArrayList<LianXiRenMod>();
        SQLiteDatabase db = myDatebase.getReadableDatabase();

        Cursor cursor= db.rawQuery("select username,niche,zhuangtai,touxiang from lianxiren where groupname=?", new String[]{groupname});
        while(cursor.moveToNext()){
            LianXiRenMod lxr=new LianXiRenMod();
            lxr.setUsername(cursor.getString(0));
            lxr.setNicheng(cursor.getString(1));
            lxr.setZhuangtai(cursor.getString(2));
            lxr.setTouxiang_url(cursor.getString(3));
            list.add(lxr);
        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     * 获取联系人详细列表
     *
     * @return
     */
    public HashMap<String,List<LianXiRenMod>> getLianxirenList(){
        HashMap<String,List<LianXiRenMod>> list = new HashMap<String,List<LianXiRenMod>>();
        List<String> lxrgroup=getLianxirenGroup();

        for (int i=0;i<lxrgroup.size();i++){
            list.put(lxrgroup.get(i),getLianxiren(lxrgroup.get(i)));
        }

        return list;
    }

    /**
     * 所有联系人入库
     *
     * @param roster
     */
    public void setLianxirenAll(Roster roster){
        SQLiteDatabase db = myDatebase.getWritableDatabase();

        List<RosterGroup> LxrGroup=LianxirenList.getLxrGroup(roster);
        HashMap<String,List<RosterEntry>> lianxiren=LianxirenList.getLianxiren(roster);
        ContentValues values = new ContentValues();


        for (int i=0;i<LxrGroup.size();i++){

            for (int j=0;j<lianxiren.get(LxrGroup.get(i).getName()).size();j++){
                String username=GetName.getName(lianxiren.get(LxrGroup.get(i).getName()).get(j).getUser());
                values.put("username", username);
                values.put("groupname", LxrGroup.get(i).getName());
                values.put("niche", friendInfo.getNiCheng(lianxiren.get(LxrGroup.get(i).getName()).get(j).getUser()));
                values.put("zhuangtai", friendInfo.getZhuangTai(lianxiren.get(LxrGroup.get(i).getName()).get(j).getUser()));
                try{
                    System.out.println(username+"不在数据库,添加到数据库");
                    db.insert("lianxiren", null, values);
                }catch(Exception e) {
                    System.out.println(username+"已在数据库,更新数据");
                    db.update("lianxiren", values, "username=?", new String[]{username});
                }


            }
        }

        db.close();
    }

    /**
     * 获取全部联系人名字
     *
     * @return List
     */
    public List<String> getLXRnameAll(){
        List<String> list=new ArrayList<String>();
        SQLiteDatabase db =myDatebase.getReadableDatabase();
        Cursor cursor= db.rawQuery("select username from lianxiren", new String[]{});
        while (cursor.moveToNext()){
            list.add(cursor.getString(0));
        }
        db.close();
        return list;

    }

    /**
     * 获得所有联系人昵称
     * @return
     */
    public List<QueryHaoyouMod> getLXRnichengAll(){
        List<QueryHaoyouMod> list=new ArrayList<QueryHaoyouMod>();
        SQLiteDatabase db =myDatebase.getReadableDatabase();
        Cursor cursor= db.rawQuery("select username,niche from lianxiren", new String[]{});

        while (cursor.moveToNext()){
            QueryHaoyouMod queryHaoyouMod = new QueryHaoyouMod();
            queryHaoyouMod.setUsername(cursor.getString(0));
            queryHaoyouMod.setNick(cursor.getString(1));
            list.add(queryHaoyouMod);
        }
        db.close();
        return list;
    }

    /**
     * 设置全部联系人昵称
     */
    public void setNichenAll(HashMap<String,String> nichenlist){
        SQLiteDatabase db =myDatebase.getWritableDatabase();
        ContentValues values= new ContentValues();

        for (Map.Entry<String, String> entry : nichenlist.entrySet()) {
            values.put("niche",  entry.getValue().toString());
            db.update("lianxiren", values, "username=?", new String[]{entry.getKey().toString()});
        }
        db.close();
    }
    /**
     * 根据用户名获取昵称
     */
    public String getNichen(String username){
        SQLiteDatabase db = myDatebase.getReadableDatabase();
        Cursor cursor=db.rawQuery("select niche from lianxiren where username=?", new String[]{GetName.getName(username)});
        String nichen=null;
        while (cursor.moveToNext()){
            nichen=cursor.getString(0);
        }
        System.out.println("获取昵称="+nichen);
        db.close();
        if(nichen!=null)
            return nichen;
        else
            return username;
    }
    /**
     * 根据昵称获得用户名
     */
    public String getUsername(String nicheng){
        SQLiteDatabase db = myDatebase.getReadableDatabase();
        try {
            Cursor cursor=db.rawQuery("select username from lianxiren where niche=?", new String[]{nicheng});
            String username=null;
            while (cursor.moveToNext()){
                username=cursor.getString(0);
            }
            db.close();
            if(username!=null)
                return username;
            else
                return nicheng;
        }catch (Exception e){
            return nicheng;
        }



    }
    /**
     * 根据用户名删除好友
     */
    public void removeUser(String username){
        SQLiteDatabase db =myDatebase.getWritableDatabase();
        db.delete("lianxiren","username=?",new String[]{username});
        System.out.println("从数据库中删除好友：" + username);
        db.close();
    }
    /**
     * 根据用户名加好友
     */
    public void addUser(String username){
        SQLiteDatabase db =myDatebase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", GetName.getName(username));
        values.put("groupname", "Friends");
        values.put("niche",friendInfo.getNiCheng(username));
        values.put("zhuangtai", "");

        db.insert("lianxiren", null, values);
        db.close();
    }


}
