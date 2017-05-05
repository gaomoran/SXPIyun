package com.mrg.sxpiyun.adpter;

import android.content.Context;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxg.sxpi.ui.R;
import com.mrg.sxpiyun.modle.LianXiRenMod;

import java.util.HashMap;
import java.util.List;

/**
 * 用于实现联系人列表填充的适配器
 * Created by MrG on 2016-07-18.
 */
public class LianxirenAdpter extends BaseExpandableListAdapter {
    // 上下文
    private Context context=null ;
    //联系人分组
    private List<String> lxrGroup=null;
    //联系人详细
    private HashMap<String, List<LianXiRenMod>> lianxiren=null;

    public LianxirenAdpter(Context context, List<String> LxrGroup,HashMap<String,List<LianXiRenMod>> lxr) {
        this.context=context;
        this.lxrGroup=LxrGroup;
        this.lianxiren=lxr;
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
        return lianxiren.get(lxrGroup.get(groupPosition)).get(childPosition);
    }
    //返回分组数据个数
    @Override
    public int getChildrenCount(int groupPosition) {

        return lianxiren.get(lxrGroup.get(groupPosition)).size();
    }

    // 按组下标返回组的视图
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView=View.inflate(context, R.layout.lianxiren_group,null);
        TextView groupname=(TextView)convertView.findViewById(R.id.groupname);
        TextView number=(TextView)convertView.findViewById(R.id.onlineno);

       //设置当前组组名
        groupname.setText(lxrGroup.get(groupPosition));
       //设置当前组有几个成员
        number.setText("["+lianxiren.get(lxrGroup.get(groupPosition)).size()+"]");

        return convertView;
    }
//返回组里的成员视图 根据下标显示数据
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        convertView =View.inflate(context,R.layout.lianxiren_item,null);
        ImageView touxiang = (ImageView)convertView.findViewById(R.id.child_item_head);
        TextView username =(TextView)convertView.findViewById(R.id.username);
        TextView mod = (TextView)convertView.findViewById(R.id.mood);
        String uname=lianxiren.get(lxrGroup.get(groupPosition)).get(childPosition).getUsername();
        //获取好友名字
        String  name=lianxiren.get(lxrGroup.get(groupPosition)).get(childPosition).getNicheng();
        if (name==null) {
            username.setText(uname);
        }
        else {
            username.setText(name);
        }

        //获取好友头像
       // Bitmap touxiangbp = FriendInfo.getTouXiang(uname, context);
       // touxiang.setImageBitmap(touxiangbp);

        //获取好友状态
        mod.setText(lianxiren.get(lxrGroup.get(groupPosition)).get(childPosition).getZhuangtai());
        return convertView;
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
