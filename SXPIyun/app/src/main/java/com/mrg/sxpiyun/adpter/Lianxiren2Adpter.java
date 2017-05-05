package com.mrg.sxpiyun.adpter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxg.sxpi.ui.R;
import com.mrg.sxpiyun.modle.LianXiRenMod;
import com.mrg.sxpiyun.util.FriendInfo;
import com.mrg.sxpiyun.util.TouXiangGet;

import java.util.HashMap;
import java.util.List;

/**
 * 用于实现联系人列表填充的适配器
 * Created by MrG on 2016-07-18.
 */
public class Lianxiren2Adpter extends BaseExpandableListAdapter {
    // 上下文
    private Context context=null ;
    //联系人分组
    private List<String> lxrGroup=null;
    //联系人详细
    private HashMap<String, List<LianXiRenMod>> lianxiren=null;

    private FriendInfo friendInfo=new FriendInfo();

    public Lianxiren2Adpter(Context context, List<String> LxrGroup,HashMap<String,List<LianXiRenMod>> lxr) {
        this.context=context;
        this.lxrGroup=LxrGroup;
        this.lianxiren=lxr;
    }
    public void ShuaxinLianxirenList(List<String> LxrGroup,HashMap<String,List<LianXiRenMod>> lxr){
        this.lxrGroup.clear();
        this.lxrGroup=LxrGroup;
        this.lianxiren.clear();
        this.lianxiren=lxr;
        notifyDataSetChanged();

    }



    //返回组的个数
    @Override
    public int getGroupCount() {

        return lxrGroup.size();
    }
    // 按下标返回组名
    @Override
    public Object getGroup(int groupPosition) {

        return lxrGroup.get(groupPosition);
    }
    // 按下标返回分组内好友数据
    @Override
    public Object getChild(int groupPosition, int childPosition) {

        return lianxiren.get(lxrGroup.get(groupPosition));
    }
    //返回分组数据个数
    @Override
    public int getChildrenCount(int groupPosition) {

        return lianxiren.get(lxrGroup.get(groupPosition)).size();
    }

    // 按组下标返回组的视图
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        if(convertView==null) {
            view = View.inflate(context, R.layout.lianxiren_group, null);
            viewHolder=new ViewHolder();
            viewHolder.groupname =(TextView)view.findViewById(R.id.groupname);
            viewHolder. number=(TextView)view.findViewById(R.id.onlineno);
            view.setTag(viewHolder);
        }else {
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
        }
        //设置当前组组名
        viewHolder.groupname.setText(lxrGroup.get(groupPosition));
        //设置当前组有几个成员
        viewHolder.number.setText("[" + lianxiren.get(lxrGroup.get(groupPosition)).size() + "]");

        return view;
    }
    //返回组里的成员视图 根据下标显示数据
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        if(convertView==null) {
            view = View.inflate(context, R.layout.lianxiren_item, null);
            viewHolder=new ViewHolder();
            viewHolder. touxiang = (ImageView) view.findViewById(R.id.child_item_head);
            viewHolder. username = (TextView) view.findViewById(R.id.tv_lianxiren_nicheng);
            viewHolder. mod = (TextView) view.findViewById(R.id.mood);
            view.setTag(viewHolder);

        }
        else {
            view=convertView;
            //获取好友名字
            viewHolder=(ViewHolder)view.getTag();
        }
        String name = lianxiren.get(lxrGroup.get(groupPosition)).get(childPosition).getUsername();
        String nicheng=lianxiren.get(lxrGroup.get(groupPosition)).get(childPosition).getNicheng();
        if(nicheng!=null){
            viewHolder.username.setText(nicheng);
        }else {
            viewHolder.username.setText(name);
        }
        TouXiangGet touXiangGet=new TouXiangGet();
            //获取好友头像
            Bitmap touxiangbp = touXiangGet.getTouxiang(name,context);
            viewHolder.touxiang.setImageBitmap(touxiangbp);
            //获取好友状态
            viewHolder.mod.setText(friendInfo.getZhuangTai(name));


        return view;
    }
    static class  ViewHolder{
        public TextView groupname=null;
        public TextView number=null;
        public ImageView touxiang=null;
        public TextView username=null;
        public TextView mod=null;

    }


    //是否开启子菜单点击。
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }



    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }




}

