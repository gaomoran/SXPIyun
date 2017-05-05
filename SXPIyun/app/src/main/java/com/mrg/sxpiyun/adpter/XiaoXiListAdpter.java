package com.mrg.sxpiyun.adpter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zxg.sxpi.ui.R;
import com.mrg.sxpiyun.db.XiaoXiListBD;
import com.mrg.sxpiyun.db.LianXiRenBD;
import com.mrg.sxpiyun.modle.ZuiXinXiaoXi;
import com.mrg.sxpiyun.util.DataTimeUtil;
import com.mrg.sxpiyun.util.GetName;
import com.mrg.sxpiyun.util.TouXiangGet;
import com.mrg.sxpiyun.widget.BadgeView;
import com.mrg.sxpiyun.widget.YuanBitmap;

import org.jivesoftware.smack.packet.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrG on 2016-07-22.
 */
public class XiaoXiListAdpter extends BaseAdapter{

    private TextView title,newxx,time;
    private BadgeView viewtag;
    private YuanBitmap touxiang;
    private Context context=null;
    private List<ZuiXinXiaoXi> zuixinxiaoxilist=new ArrayList<ZuiXinXiaoXi>();
    private ZuiXinXiaoXi zxxx=null;
    private XiaoXiListBD xiaoXiListBD;
    private int msgshu=1;
    private TextView username;
    private LianXiRenBD lianXiRenBD;

    public XiaoXiListAdpter(Context context,List<ZuiXinXiaoXi> zuixinxiaoxilist,XiaoXiListBD xiaoXiListBD){
        this.context=context;
        this.zuixinxiaoxilist=zuixinxiaoxilist;
        this.xiaoXiListBD=xiaoXiListBD;
        lianXiRenBD = new LianXiRenBD(context);
    }
    public void addXiaoxi(Message message){
        //判断最新消息链表是否为空
        if(xiaoXiListBD.quertIsExist(GetName.getName(message.getFrom()))){
            int msgshu=xiaoXiListBD.quertNewMsgShu(GetName.getName(message.getFrom()));
            xiaoXiListBD.removeXiaoxi(GetName.getName(message.getFrom()));
            xiaoXiListBD.addNewChatMsg(message,msgshu+1,System.currentTimeMillis());

        }else {
            xiaoXiListBD.addNewChatMsg(message,msgshu,System.currentTimeMillis());
        }
        zuixinxiaoxilist=xiaoXiListBD.quertAllXiaoXi();
        //刷新列表
        notifyDataSetChanged();


    }
    public void suaxinadpter(){
        zuixinxiaoxilist=xiaoXiListBD.quertAllXiaoXi();
        //刷新列表
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return zuixinxiaoxilist.size();
    }

    @Override
    public Object getItem(int position) {
        return zuixinxiaoxilist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(context,R.layout.xiaoxilist_item,null);
        title=(TextView)convertView.findViewById(R.id.tv_xiaoxilist_title);
        newxx=(TextView)convertView.findViewById(R.id.tv_xiaoxilist_xxkan);
        time=(TextView)convertView.findViewById(R.id.tv_xiaoxilist_time);
        viewtag=(BadgeView)convertView.findViewById(R.id.tv_xiaoxilist_newxx);
        touxiang=(YuanBitmap)convertView.findViewById(R.id.img_xiaoxilist_touxiang);
        username = (TextView) convertView.findViewById(R.id.tv_xiaoxilist_username);
        //获取头像//需要优化；
        touxiang.setImageBitmap(new TouXiangGet().getTouxiang(zuixinxiaoxilist.get(position).getName(),context));
       //设置标题
        title.setText(zuixinxiaoxilist.get(position).getTitle());
        username.setText(zuixinxiaoxilist.get(position).getName());
       //设置消息预览
        String newMsg=zuixinxiaoxilist.get(position).getNews();
       /* try {
            //超过22字符裁剪
            newMsg=newMsg.substring(0,18);
            newMsg+="...";
        }catch (Exception e){
            //不裁剪
        }*/
        newxx.setText(newMsg);
        //设置时间
        String datatime=DataTimeUtil.getFormatDataTime(zuixinxiaoxilist.get(position).getData());

        time.setText(datatime);
        //设置消息气泡
        if(zuixinxiaoxilist.get(position).getMsgshu()==0){
            viewtag.setVisibility(View.GONE);
        }else {
            viewtag.setVisibility(View.VISIBLE);
            viewtag.setText(zuixinxiaoxilist.get(position).getMsgshu() + "");
        }


        return convertView;
    }
}
