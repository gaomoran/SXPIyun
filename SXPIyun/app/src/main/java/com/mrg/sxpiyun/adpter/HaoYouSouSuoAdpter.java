package com.mrg.sxpiyun.adpter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxg.sxpi.ui.R;
import com.mrg.sxpiyun.modle.QueryHaoyouMod;
import com.mrg.sxpiyun.util.TouXiangGet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrG on 2016-10-10.
 */
public class HaoYouSouSuoAdpter extends BaseAdapter{

    List<QueryHaoyouMod> haoyoulist=new ArrayList<QueryHaoyouMod>();
    private TouXiangGet touXiangGet=new TouXiangGet();
    Context context=null;
    public HaoYouSouSuoAdpter(Context context){
        this.context=context;

    }
    public void quertuser(List<QueryHaoyouMod> haoyoulist){
        this.haoyoulist=haoyoulist;

        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return haoyoulist.size();
    }

    @Override
    public Object getItem(int position) {
        return haoyoulist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view ;
        HYViewHolder viewHolder;
        if(convertView==null){
            view=View.inflate(context, R.layout.haoyousousuo,null);
            viewHolder=new HYViewHolder();
            viewHolder.username=(TextView)view.findViewById(R.id.tv_sousuohaoyou_username);
            viewHolder.touxiang=(ImageView)view.findViewById(R.id.img_sousuohaoyou_touxiang);
            viewHolder.nichen=(TextView)view.findViewById(R.id.tv_sousuohaoyou_nichen);
            view.setTag(viewHolder);
        }else {
            view=convertView;
            viewHolder=(HYViewHolder)view.getTag();
        }
        //查询出来的好友名字
        String usrname=haoyoulist.get(position).getUsername();
        viewHolder.username.setText(usrname);
        viewHolder.nichen.setText(haoyoulist.get(position).getNick());
        viewHolder.touxiang.setImageBitmap(touXiangGet.getTouxiang(usrname,context));


        return view;
    }
    static class  HYViewHolder{
        public ImageView touxiang=null;
        public ImageView addhaoyou=null;
        public TextView nichen=null;
        public TextView username=null;


    }

}
