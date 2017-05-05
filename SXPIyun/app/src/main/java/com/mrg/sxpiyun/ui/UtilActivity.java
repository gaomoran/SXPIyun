package com.mrg.sxpiyun.ui;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.zxg.sxpi.ui.R;
import com.mrg.sxpiyun.util.UserAdmin;

public class UtilActivity extends AppCompatActivity {

    private String type;
    private FragmentTransaction beginTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_util);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        android.support.v4.app.FragmentManager supportFragmentManager = getSupportFragmentManager();
        beginTransaction = supportFragmentManager.beginTransaction();

        //根据类型
        if (type.equals("请假")) {
            if (UserAdmin.truename.equals("")||UserAdmin.classs.equals("")){
                Toast.makeText(this, "首次使用需要登陆教务系统", Toast.LENGTH_LONG).show();
                Intent intent2 = new Intent(this, JiaoWuLoginActivity.class);
                intent2.putExtra("type","请假");
                startActivityForResult(intent2, 78);
            }else {
                beginTransaction.add(R.id.fl_util_content, new LeaveFragment(), "leave_util");
                beginTransaction.commit();

            }
        }else if (type.equals("联系人")){
            beginTransaction.add(R.id.fl_util_content, new LianxirenFragment(), null);
            beginTransaction.commit();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==78&&resultCode==2){
            beginTransaction.add(R.id.fl_util_content, new LeaveFragment(), "leave_util");
            beginTransaction.commit();
        }else{
            finish();
        }


    }

    public void top_backward(View view){

        finish();
    }
}
