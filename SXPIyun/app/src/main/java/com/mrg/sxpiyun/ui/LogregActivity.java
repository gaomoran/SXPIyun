package com.mrg.sxpiyun.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.zxg.sxpi.ui.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.mrg.sxpiyun.modle.UserRegMod;
import com.mrg.sxpiyun.util.Connent;

public class LogregActivity extends Activity {
    private final int REGLOGTRUE=1;
    private EditText et_xuehao, et_name, et_nichen, et_password, et_qrpassword, et_email;
    private TextView tv_xuehao, tv_name, tv_nichen, tv_password, tv_qrpassword, tv_email;
    private RadioButton rd_xueshen, rd_laoshi;
    private Button btn_reg;
    private UserRegMod user;
    private final int REGLOGMSG=1;
   private Handler handler=new Handler(){
       @Override
       public void handleMessage(Message msg) {
           switch (msg.what){
               case REGLOGMSG:
                   String xiaoxi = msg.getData().getString("xiaoxi");
                   Toast.makeText(getApplication(), xiaoxi,Toast.LENGTH_SHORT).show();
                    if (xiaoxi.equals("恭喜你注册成功")) {
                        Intent intent = new Intent();
                        intent.putExtra("nichen", user.getNicheng());
                        intent.putExtra("email", user.getEmail());
                        intent.putExtra("username", user.getXuehao());
                        intent.putExtra("password", user.getPassword());
                        setResult(REGLOGMSG, intent);
                        finish();
                    }
                   break;
           }
       }
   };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logreg);
        TextView textView = (TextView)findViewById(R.id.tv_top_title);
        textView.setText("用户注册");

        initia();
        rd_xueshen.setOnClickListener(new danxuan());
        rd_laoshi.setOnClickListener(new danxuan());
        btn_reg.setOnClickListener(new regJiancha());
        et_email.addTextChangedListener(new bianhua());
        et_password.addTextChangedListener(new bianhua());
        et_qrpassword.addTextChangedListener(new bianhua());
        et_xuehao.addTextChangedListener(new bianhua());
    }

    /***
     * 初始化控件
     */
    public void initia() {
        et_xuehao = (EditText) findViewById(R.id.et_xuehao);
        et_nichen = (EditText) findViewById(R.id.et_nicheng);
        et_password = (EditText) findViewById(R.id.et_password);
        et_qrpassword = (EditText) findViewById(R.id.et_qrpassword);
        et_email = (EditText) findViewById(R.id.et_email);
        tv_xuehao = (TextView) findViewById(R.id.tv_xuehao);
        tv_nichen = (TextView) findViewById(R.id.tv_nichen);
        tv_password = (TextView) findViewById(R.id.tv_password);
        tv_qrpassword = (TextView) findViewById(R.id.tv_qrpassword);
        tv_email = (TextView) findViewById(R.id.tv_maile);
        rd_xueshen = (RadioButton) findViewById(R.id.rd_xueshen);
        rd_laoshi = (RadioButton) findViewById(R.id.rd_laoshi);
        btn_reg = (Button) findViewById(R.id.btn_reg);
        user=new UserRegMod();

    }

    private class danxuan implements View.OnClickListener {

            /*使两个单选按钮只能一次选择其中一个*/
            public void onClick(View v) {
                if(v.getId()==R.id.rd_laoshi){
                    rd_laoshi.setChecked(true);
                    rd_xueshen.setChecked(false);
                }else {
                    rd_laoshi.setChecked(false);
                    rd_xueshen.setChecked(true);
                }
            }
    }

    private boolean tfnull(){
        int weikon=1;

        if( et_email.getText().length()==0) {
            weikon=0;
            tv_email.setText("用于找回密码，不能为空");
        }

        if(et_xuehao.getText().length()==0) {
            weikon=0;
            tv_xuehao.setText("学号不能为空");
        }

        if(et_password.getText().length()==0) {
            weikon=0;
            tv_password.setText("密码不能为空");
        }

        if(et_qrpassword.getText().length()==0) {
            weikon=0;
            tv_qrpassword.setText("密码不能为空");
        }


        if(weikon==1)
            return true;
        else
            return false;
    }
    /**
     * 当文本框内容变化是调用
     * */
    private class bianhua implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if(count!=after){
                tv_qrpassword.setText("");
                tv_password.setText("");
                tv_xuehao.setText("");
                tv_email.setText("");

            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
    /**
     * 判断邮箱是否合法
     * @param email
     * @return
     */
    public boolean isEmail(String email){
        if (null==email || "".equals(email)) return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }


    /**
     * 点击注册按钮时调用的方法
     * */
    private class regJiancha implements View.OnClickListener{

    @Override
    public void onClick(View v) {
        int istrue=1;
        if(tfnull()){

            if (null==et_xuehao.getText().toString()){
                tv_xuehao.setText("不要使用非法字符、中文");
            }else {
                user.setXuehao(et_xuehao.getText().toString());
            }

            if (et_nichen.getText().length()==0){
                user.setNicheng("未定");
            }else {
                user.setNicheng(et_nichen.getText().toString());
            }
            if (null==et_password.getText().toString()){
                tv_password.setText("密码不能为空");
            }else {
                if (et_password.getText().toString().equals(et_qrpassword.getText().toString()))
                    user.setPassword(et_password.getText().toString());
                else {
                    tv_qrpassword.setText("两次输入密码不一致");
                    istrue = 0;
                }
            }
            if(isEmail(et_email.getText().toString())){
                user.setEmail(et_email.getText().toString());
            }else {
                tv_email.setText("邮箱格式不正确");
                istrue=0;
            }
            /*模拟提交*/
            if(istrue==1){
                Thread thread = new Thread(){
                    @Override
                    public void run() {
                        Connent connent = new Connent();
                        if (connent.onConnet()){
                            String regist = connent.regist(user.getXuehao(),user.getPassword());
                            Message message = handler.obtainMessage();
                            Bundle data = new Bundle();
                            data.putString("xiaoxi",regist);
                            message.setData(data);
                            message.what=REGLOGMSG;
                            handler.sendMessage(message);
                        }else {
                            System.out.println("连接服务器失败");
                        }
                    }


                };
                thread.start();


            }


        }
    }
}
    public void top_backward(View v){
        finish();
    }

}
