package com.mrg.sxpiyun.ui;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zxg.sxpi.ui.R;
import com.mrg.sxpiyun.adpter.SelectResultsAdpter;
import com.mrg.sxpiyun.modle.Results;
import com.mrg.sxpiyun.util.HttpHelper;
import com.mrg.sxpiyun.util.UserAdmin;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

public class SelectResultsActivity extends AppCompatActivity {

    private ListView listView;
    private HttpHelper httpHelper;
    private File ff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_results);
        TextView textView = (TextView)findViewById(R.id.tv_top_title);
        textView.setText("成绩查询");
        TextView toplist = (TextView)findViewById(R.id.btn_top_forward);
        toplist.setVisibility(View.VISIBLE);
        toplist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(v);
            }
        });
        listView = (ListView) findViewById(R.id.lv_selectresults_result);
        httpHelper = new HttpHelper();
        if ( UserAdmin.truename.equals("") || UserAdmin.classs.equals("")){
            Toast.makeText(this, "首次使用需要登陆教务系统", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, JiaoWuLoginActivity.class);
            intent.putExtra("type","成绩");
            startActivityForResult(intent, 233);
        }else {
            String path = Environment.getExternalStorageDirectory().toString();
            ff = new File(path + "/SXPIyun/chenji/" + UserAdmin.username + ".html");

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
                List<Results> results = httpHelper.getResults(parse);
                listView.setAdapter(new SelectResultsAdpter(this, results));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==233&&resultCode==3){
            List<Results> results = (List<Results>) data.getSerializableExtra("results");
            SelectResultsAdpter adpter = new SelectResultsAdpter(this, results);
            listView.setAdapter(adpter);
        }
    }
    private void showPopupWindow(final View view) {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(this).inflate(
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
                intent.putExtra("type","成绩");
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

    public void top_backward(View v){
        finish();

    }
}
