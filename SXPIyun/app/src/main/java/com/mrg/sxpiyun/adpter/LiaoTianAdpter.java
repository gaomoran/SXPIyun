package com.mrg.sxpiyun.adpter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mrg.sxpiyun.ui.LiaotianActivity;
import com.zxg.sxpi.ui.R;
import com.mrg.sxpiyun.modle.XiaoxiMod;
import com.mrg.sxpiyun.util.GetName;
import com.mrg.sxpiyun.util.TouXiangGet;
import com.mrg.sxpiyun.util.UserAdmin;
import com.mrg.sxpiyun.widget.YuanBitmap;

import org.jivesoftware.smack.packet.Message;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by MrG on 2016-07-19.
 */
public class LiaoTianAdpter extends BaseAdapter {

    private List<Message> list =new ArrayList<Message>();
    private XiaoxiMod lishixiaoxi;
    private Context context=null;
    private Bitmap fromToxiang=null;
    private String fname=null;
    private int flag=0;
    private TouXiangGet touXiangGet=new TouXiangGet();

    public LiaoTianAdpter(Context context,List<Message> lishixiaoxi,String username) {
        this.context=context;
        fname=username;
        list=lishixiaoxi;

        fromToxiang=touXiangGet.getTouxiang(username,context);
        flag=1;

    }

    /**
     * 添加一条新消息
     * @param message
     */
    public void addMessage(Message message){
        list.add(message);
        //刷新列表
        notifyDataSetChanged();
       LiaotianActivity.liaotian.setSelection(LiaotianActivity.liaotian.getBottom());
    }

    /**
     * 获取当前聊天记录
     * @return
     */
    public List<Message> getLiaoTianJiLu(){
        return list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position,View convertView,ViewGroup parent){

        Message msg = list.get(position);
        ViewHolde viewHolde;
        if(convertView==null||(viewHolde=(ViewHolde)convertView.getTag()).flag!=GetName.getName(msg.getTo())){
            if(!UserAdmin.username.equals(GetName.getName(msg.getTo()))) {
                //发送
                viewHolde=new ViewHolde();
                convertView=View.inflate(context, R.layout.formclient_chat_out,null);
                viewHolde.msg=(TextView)convertView.findViewById(R.id.liaotian_out_msg);
                viewHolde.touxiang=(YuanBitmap)convertView.findViewById(R.id.from_head);


            }else {
                viewHolde=new ViewHolde();
                convertView=View.inflate(context, R.layout.formclient_chat_in, null);
                viewHolde.msg=(TextView) convertView.findViewById(R.id.liaotian_in_msg);
                viewHolde.touxiang= (YuanBitmap) convertView.findViewById(R.id.from_head);
            }

            viewHolde.flag=GetName.getName(msg.getTo());
            convertView.setTag(viewHolde);
        }else {
            viewHolde= (ViewHolde) convertView.getTag();
        }
        if(!UserAdmin.username.equals(GetName.getName(msg.getTo()))) {

            viewHolde.touxiang.setImageBitmap(touXiangGet.getTouxiang(UserAdmin.username,context));
        }else {
            viewHolde.touxiang.setImageBitmap(fromToxiang);
        }
        viewHolde.msg.setText(msg.getBody().toString());



        return convertView;







        /**
         *  View fasunview;
         View jieshouview;
        if(!UserAdmin.username.equals(GetName.getName(msg.getTo()))){
            //发送

            fasunview=View.inflate(context, R.layout.formclient_chat_out,null);
            TextView outmsg =(TextView)fasunview.findViewById(R.id.liaotian_out_msg);
            YuanBitmap outTouxiang=(YuanBitmap)fasunview.findViewById(R.id.from_head);

            outmsg.setText(msg.getBody().toString());
            outTouxiang.setImageBitmap(UserAdmin.myTouxiang);

            return fasunview;

        }else {
            //接收
                System.out.println(" 创建发送视图---》");
                jieshouview = View.inflate(context, R.layout.formclient_chat_in, null);
                TextView inmsg = (TextView) jieshouview.findViewById(R.id.liaotian_in_msg);
                YuanBitmap fromTouxiang = (YuanBitmap) jieshouview.findViewById(R.id.from_head);
                fromTouxiang.setImageBitmap(fromToxiang);

                inmsg.setText(msg.getBody().toString());

            return jieshouview;
        }
         */


    }
    static class ViewHolde{
        public static TextView msg;
        public static YuanBitmap touxiang;
        public static String  flag;

    }

 }
