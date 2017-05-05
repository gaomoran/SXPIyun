package com.mrg.sxpiyun.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zxg.sxpi.ui.R;
import com.mrg.sxpiyun.modle.Schedle;
import com.mrg.sxpiyun.util.HttpHelper;
import com.mrg.sxpiyun.util.UserAdmin;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;


public class ScheduleFragment extends Fragment {

    private View view;
    private HttpHelper httpHelper;
    private String path;
    private File ff;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_schedule, container, false);
        TextView textView = (TextView) view.findViewById(R.id.tv_top_title);
        TextView toplist = (TextView) view.findViewById(R.id.btn_top_forward);
        textView.setText("个人课表");
        TextView backward = (TextView) view.findViewById(R.id.btn_top_backward);
        backward.setVisibility(View.GONE);
        toplist.setVisibility(View.VISIBLE);
        toplist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(v);
            }
        });
        httpHelper = new HttpHelper();
        path = Environment.getExternalStorageDirectory().toString();
        ff = new File(path + "/SXPIyun/kebiao/" + UserAdmin.username + ".html");
        //判断是否登陆过
        if (UserAdmin.truename.equals("") || UserAdmin.classs.equals("")) {
            Toast.makeText(getActivity(), "首次使用需要登陆教务系统", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getActivity(), JiaoWuLoginActivity.class);
            intent.putExtra("type","课表");
            startActivityForResult(intent, 2);
        } else {
            try {
                String ss = "";
                String sf = "";
                InputStreamReader read = new InputStreamReader(new FileInputStream(ff), "gb2312");
                BufferedReader bufferedReader = new BufferedReader(read);

                while ((ss = bufferedReader.readLine()) != null) {
                    sf += ss;

                }
                read.close();
                bufferedReader.close();
                Document parse = Jsoup.parse(sf.toString());
                List<Schedle> schedules = httpHelper.getSchedule(parse);
                loadkebiao(schedules);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return view;
    }

    /**
     * 加载课程表
     *
     * @param schedles
     */
    private void loadkebiao(List<Schedle> schedles) {
        LinearLayout ll_Monday = (LinearLayout) view.findViewById(R.id.ll_schedule_Monday);
        LinearLayout ll_Tuesday = (LinearLayout) view.findViewById(R.id.ll_schedule_Tuesday);
        LinearLayout ll_Wednesday = (LinearLayout) view.findViewById(R.id.ll_schedule_Wednesday);
        LinearLayout ll_Thursday = (LinearLayout) view.findViewById(R.id.ll_schedule_Thursday);
        LinearLayout ll_Friday = (LinearLayout) view.findViewById(R.id.ll_schedule_Friday);
        int[][] kb = new int[9][6];
        for (Schedle sc : schedles) {
            kb[sc.getNo()][sc.getWeek()] = 1;

        }
        for (Schedle schedle : schedles) {
            TextView textView = new TextView(getActivity());
            textView.setText(schedle.getCourses() + "\n[" + schedle.getTeacher() + "]");

            switch ((int) (Math.random() * 4+1)) {
                case 1:
                    textView.setBackgroundResource(R.drawable.yuanjiao_kebiao_huan);
                    break;
                case 2:
                    textView.setBackgroundResource(R.drawable.yuanjiao_kebiao_lv);
                    break;
                case 3:
                    textView.setBackgroundResource(R.drawable.yuanjiao_kebiao_zi);
                    break;
                case 4:
                    textView.setBackgroundResource(R.drawable.yuanjiao_kebiao_lan);
                    break;

            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300);

            if (schedle.getWeek() == 1) {

                layoutParams.setMargins(0, getweizhi(schedle, kb), 0, 0);
                ll_Monday.addView(textView, layoutParams);
            } else if (schedle.getWeek() == 2) {

                layoutParams.setMargins(0, getweizhi(schedle, kb), 0, 0);
                ll_Tuesday.addView(textView, layoutParams);

            } else if (schedle.getWeek() == 3) {
                layoutParams.setMargins(0, getweizhi(schedle, kb), 0, 0);
                ll_Wednesday.addView(textView, layoutParams);

            } else if (schedle.getWeek() == 4) {
                layoutParams.setMargins(0, getweizhi(schedle, kb), 0, 0);
                ll_Thursday.addView(textView, layoutParams);

            } else {
                layoutParams.setMargins(0, getweizhi(schedle, kb), 0, 0);
                ll_Friday.addView(textView, layoutParams);

            }
        }


    }

    /**
     * 课程表布局用
     *
     * @param schedle
     * @param kb
     * @return
     */
    private int getweizhi(Schedle schedle, int[][] kb) {
        if (schedle.getNo() == 1) {
            return 0;
        }
        int ss = 0;
        for (int i = 1; i < schedle.getNo(); i++) {
            if (kb[i][schedle.getWeek()] == 1) {
                ss = i;
            }
        }
        if ((schedle.getNo() - ss) == 2) {
            return 0;
        } else if ((schedle.getNo() - ss) == 4 || (schedle.getNo() - ss) == 3) {
            return 300;
        } else if ((schedle.getNo() - ss) == 6 || (schedle.getNo() - ss) == 5) {
            return 600;
        } else {
            return 900;
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == 2) {
            List<Schedle> schedle = (List<Schedle>) data.getSerializableExtra("schedle");
            loadkebiao(schedle);
        }
    }

    private void showPopupWindow(final View view) {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(getActivity()).inflate(
                R.layout.jiaowu_pop_window, null);

        TextView genghuan = (TextView) contentView.findViewById(R.id.tv_pop_genghuan);
        // 设置按钮的点击事件
        genghuan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ff.exists()) {
                    ff.delete();
                }
                Intent intent = new Intent(view.getContext(), JiaoWuLoginActivity.class);
                intent.putExtra("type","课表");
                startActivity(intent);

            }
        });

        final PopupWindow popupWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setTouchable(true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.yuanjiao_edittext));

        // 设置好参数之后再show
        popupWindow.showAsDropDown(view);


    }

}
