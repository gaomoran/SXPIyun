package com.mrg.sxpiyun.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.zxg.sxpi.ui.R;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by MrG on 2016-07-30.
 */
public class TouXiangGet {
    private HashMap<String,String >nichenlist=new HashMap<String,String>();

    /**
     *  从网络获取用户头像；
      */

    private Bitmap getTouXiang(String username,Context context) {
        byte[] touxiang=null;
        VCard vCard=new VCard();
        XMPPConnection conn=ConnectionAdmin.conns;
        try {

            vCard.load(conn, GetName.getUsername(username));
            touxiang=vCard.getAvatar();

        } catch (Exception e) {
            System.out.println("-------没接上-----");
            e.printStackTrace();
            touxiang = null;

        }


        if (touxiang!=null) {
            Bitmap touxiangmap = Bytes2Bimap(touxiang);
            return touxiangmap;
        }else{
            return null;
        }




    }

    /**
     * 将byte[]转换为Bitmap;
     */

    public static Bitmap Bytes2Bimap(byte[] b) {
        int bb=b.length;
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, bb);
        } else {
            return null;
        }
    }
    /**
     *  保存头像到本地
     */

    public void saveMyBitmap(Bitmap mBitmap,String bitName)  {
        String PATH = Environment.getExternalStorageDirectory().toString();
        File ff = new File( PATH+"/SXPIyun/touxiang/");
        FileOutputStream fOut = null;
        if (!ff.exists()) {
            try {
                //按照指定的路径创建文件夹
                ff.mkdirs();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        File f = new File( PATH+"/SXPIyun/touxiang/"+GetName.getName(bitName)+ ".jpg");
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
            System.out.println(" 头像保存成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * 读取本地缓存的头像
     */
    public Bitmap getTouxiang(String username,Context context) {
        String path = Environment.getExternalStorageDirectory() + "/SXPIyun/touxiang/"+GetName.getName(username)+".jpg";
        File mfile = new File(path);
        if (mfile.exists()) {//若该文件存在

            Bitmap bm = BitmapFactory.decodeFile(path);
            System.out.println("读取"+username+"头像成功");
            return bm;
        }else{//不存在则在网络上获取

            Bitmap bm= getTouXiang(username, context);

            if (bm!=null){
                System.out.println(username+"从网络获取头像成功");
                //把头像保存到本地
                saveMyBitmap(bm,GetName.getName(username));
                return bm;
            }

        }
        System.out.println(username + "加载本地备用图片");
        //网络上也失败了就加载本地的备用图像
        Bitmap bmt = BitmapFactory.decodeResource(context.getResources(), R.drawable.faceback_head);
        return bmt;
    }

  public void removeTouXiang(String username){
      String path = Environment.getExternalStorageDirectory() + "/SXPIyun/touxiang/"+GetName.getName(username)+".jpg";
      File mfile = new File(path);
      if (mfile.exists()) {//若该文件存在
        mfile.delete();
      }
  }

}
