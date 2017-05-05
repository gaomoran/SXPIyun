package com.mrg.sxpiyun.adpter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zxg.sxpi.ui.R;
import com.mrg.sxpiyun.modle.Results;

import java.util.List;

/**
 * Created by MrG on 2016-12-12.
 */
public class SelectResultsAdpter extends BaseAdapter {

    private Context context;
    private List<Results> resultses;

    public SelectResultsAdpter(Context context, List<Results> resultses) {
        this.context = context;
        this.resultses = resultses;

    }

    @Override
    public int getCount() {
        return resultses.size();
    }

    @Override
    public Object getItem(int position) {
        return resultses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        viewholder viewholder;

        if (convertView == null) {
            view = View.inflate(context, R.layout.item_selectresults, null);
            viewholder = new viewholder();
            viewholder.tv_xuenian = (TextView) view.findViewById(R.id.tv_selectresults_xuenian);
            viewholder.tv_xueqi = (TextView) view.findViewById(R.id.tv_selectresults_xueqi);
            viewholder.tv_kechengming = (TextView) view.findViewById(R.id.tv_selectresults_kechengming);
            viewholder.tv_kechengtype = (TextView) view.findViewById(R.id.tv_selectresults_kechengtype);
            viewholder.tv_teacher = (TextView) view.findViewById(R.id.tv_selectresults_teacher);
            viewholder.tv_kaohefangshi = (TextView) view.findViewById(R.id.tv_selectresults_kaohefangshi);
            viewholder.tv_zongping = (TextView) view.findViewById(R.id.tv_selectresults_zongping);
            view.setTag(viewholder);
        } else {

            view = convertView;
            viewholder = (viewholder) view.getTag();
        }
        viewholder.tv_xuenian.setText(resultses.get(position).getXuenian());
        viewholder.tv_xueqi.setText("/" + resultses.get(position).getXueqi());
        viewholder.tv_kaohefangshi.setText(resultses.get(position).getKaohefangshi());
        viewholder.tv_kechengming.setText(resultses.get(position).getKechengming());
        viewholder.tv_kechengtype.setText(resultses.get(position).getKechengtype());
        viewholder.tv_teacher.setText(resultses.get(position).getTeacher());

        String zongping = resultses.get(position).getZongping();

        viewholder.tv_zongping.setText(zongping);


        return view;
    }

    private static class viewholder {
        public TextView tv_xuenian;
        public TextView tv_xueqi;
        public TextView tv_kechengming;
        public TextView tv_kechengtype;
        public TextView tv_teacher;
        public TextView tv_kaohefangshi;
        public TextView tv_zongping;
    }
}
