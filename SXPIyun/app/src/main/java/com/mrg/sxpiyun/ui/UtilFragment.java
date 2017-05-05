package com.mrg.sxpiyun.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zxg.sxpi.ui.R;


public class UtilFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_util, container, false);

        TextView textView = (TextView) view.findViewById(R.id.tv_top_title);
        textView.setText("工具");
        TextView backward = (TextView) view.findViewById(R.id.btn_top_backward);
        backward.setVisibility(View.GONE);
        RelativeLayout leave = (RelativeLayout) view.findViewById(R.id.rl_util_leave);
        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UtilActivity.class);
                intent.putExtra("type", "请假");
                startActivity(intent);

            }
        });
        RelativeLayout dongtai = (RelativeLayout) view.findViewById(R.id.rl_util_dongtai);
        dongtai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url", "http://www.sxpi.edu.cn/news/xydt.htm");
                startActivity(intent);
            }
        });

        RelativeLayout news = (RelativeLayout) view.findViewById(R.id.rl_util_news);
        news.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url", "http://www.sxpi.edu.cn/news/gyyw.htm");
                startActivity(intent);
                }
             }
        );

        RelativeLayout gonggao = (RelativeLayout) view.findViewById(R.id.rl_util_gonggao);
        gonggao.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url", "http://www.sxpi.edu.cn/news/notice.htm");
                startActivity(intent);
                }
             }
        );
        RelativeLayout map = (RelativeLayout) view.findViewById(R.id.rl_util_map);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapActivity.class);
                startActivity(intent);
            }
        });
        RelativeLayout select = (RelativeLayout) view.findViewById(R.id.rl_util_select);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectResultsActivity.class);
                intent.putExtra("type","成绩查询");
                startActivity(intent);
            }
        });


        return view;
    }


}
