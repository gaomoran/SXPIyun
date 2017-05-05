package com.mrg.sxpiyun.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zxg.sxpi.ui.R;
import com.mrg.sxpiyun.util.FriendInfo;

public class SetValueActivity extends AppCompatActivity {

    private EditText editText;
    private String type;
    private FriendInfo friendInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_value);
        editText = (EditText) findViewById(R.id.et_setvalue_set);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        editText.setHint(type);
        friendInfo = new FriendInfo();
        TextView textView = (TextView)findViewById(R.id.tv_top_title);
        textView.setText("修改"+type);


    }

    /**
     * 提交修改
     * @param v
     */
    public void setvalue_post(View v){

        if (editText.getText()==null){
            Toast.makeText(this,"不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if (type.equals("昵称")){

            if (friendInfo.setNiCheng(editText.getText().toString().trim())){
                Intent intent = new Intent();
                setResult(100,intent);
                Toast.makeText(this,"修改成功",Toast.LENGTH_SHORT).show();
                finish();
            }else{

                Toast.makeText(this,"修改失败",Toast.LENGTH_SHORT).show();
            }

            System.out.println("修改昵称完毕");
        }


    }

    public void top_backward(View v){
        finish();
    }
}
