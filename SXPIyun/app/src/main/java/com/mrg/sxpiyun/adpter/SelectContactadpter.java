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
 * Created by MrG on 2016-10-27.
 */
public class SelectContactadpter extends BaseAdapter {
    List<QueryHaoyouMod> contact = new ArrayList<QueryHaoyouMod>();
    Context context = null;
    private TextView nicheng;
    private ImageView touxiang;
    private TouXiangGet touXiangGet = new TouXiangGet();

    public SelectContactadpter(Context context, List<QueryHaoyouMod> contact) {
        this.contact = contact;
        this.context = context;
    }

    @Override
    public int getCount() {
        return contact.size();
    }

    @Override
    public Object getItem(int position) {
        return contact.get(position).getNick();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = View.inflate(context, R.layout.selectcontact_item, null);
        nicheng = (TextView) convertView.findViewById(R.id.tv_selectcontact_nicheng);
        touxiang = (ImageView) convertView.findViewById(R.id.img_selectcontact_touxiang);
        if (contact.get(position).getNick()==null){
            nicheng.setText(contact.get(position).getUsername());

        }else {
            nicheng.setText(contact.get(position).getNick());
        }
        touxiang.setImageBitmap(touXiangGet.getTouxiang(contact.get(position).getUsername(), context));


        return convertView;
    }

}
