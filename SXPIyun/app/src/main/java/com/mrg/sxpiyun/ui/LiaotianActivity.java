package com.mrg.sxpiyun.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.zxg.sxpi.ui.R;
import com.mrg.sxpiyun.adpter.LiaoTianAdpter;
import com.mrg.sxpiyun.db.XiaoXiBD;
import com.mrg.sxpiyun.db.LianXiRenBD;
import com.mrg.sxpiyun.util.ConnectionAdmin;
import com.mrg.sxpiyun.util.GetName;
import com.mrg.sxpiyun.util.UserAdmin;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Message;

import java.util.List;

public class LiaotianActivity extends AppCompatActivity  {

    public static ListView liaotian;
    private Button fasun;
    private EditText input;
    private TextView title;
    public static LiaoTianAdpter LTadpter;
    private Chat chat;
    private String username;
    ChatManager chatManager=ConnectionAdmin.chatManager;
    private List<Message> lishixiaoxi;
    private XiaoXiBD xiaoXiBD=new XiaoXiBD(this,UserAdmin.username);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liaotian);
        Intent intent = getIntent();

        username=GetName.getUsername(intent.getStringExtra("username"));
        System.out.println("聊天对象"+username);
        ConnectionAdmin.chatingname= GetName.getName(username);


    }
    @Override
    protected void onResume(){
        super.onResume();
        new AsyncTask<Integer, String, Integer>(){
            @Override
            protected Integer doInBackground(Integer... params) {
                input=(EditText)findViewById(R.id.et_liaotian_input);
                fasun=(Button)findViewById(R.id.btn_liaotian_fasun);
                liaotian=(ListView)findViewById(R.id.Liaotian_list);
                title=(TextView)findViewById(R.id.tv_top_title);
                publishProgress("1");
                lishixiaoxi=xiaoXiBD.getWeiduMsg(GetName.getName(username));
                System.out.println("查询历史消息"+GetName.getName(username));
                LTadpter=new LiaoTianAdpter(LiaotianActivity.this,lishixiaoxi,username);
                publishProgress("2");
                if (chatManager!=null)
                    chat= chatManager.createChat(username, null);
                publishProgress("3");

                return null;
            }
            @Override
            protected void onProgressUpdate(String... values) {
                if(values[0].toString().equals("1")) {
                    title.setText(new LianXiRenBD(getApplicationContext()).getNichen(GetName.getName(username)));
                }
                if(values[0].toString().equals("2")){
                    System.out.println("装载适配器");
                    liaotian.setAdapter(LTadpter);
                    liaotian.setSelection(liaotian.getBottom());
                }
                if (values[0].toString().equals("3")) {
                    // 点击发送按钮事件处理

                    fasun.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //获取文本框中的内容
                            String inmsg = input.getText().toString();

                            //打包成Message对象
                            Message message = new Message();
                            //类型是消息（chat)
                            message.setType(Message.Type.chat);
                            //内容是文本框中的内容
                            message.setBody(inmsg);
                            //来自“我”
                            message.setFrom(GetName.getUsername(UserAdmin.username));
                            //发给当前用户

                            System.out.println("消息发给："+GetName.getUsername(username));
                            message.setTo(username);
                            try {
                                //发送
                                chat.sendMessage(message);
                            } catch (SmackException.NotConnectedException e) {
                                e.printStackTrace();
                            }
                            //将文本框至为空方便下一条消息发送
                            input.setText("");
                            //添加到页面中
                            LTadpter.addMessage(message);
                            //添加到数据库
                            xiaoXiBD.addNewMsg(message);
                            System.out.println("添加到数据库"+message.getTo());
                            //listview移动到最下端
                            liaotian.setSelection(liaotian.getBottom());



                        }
                    });
                    if (chatManager==null){
                        fasun.setEnabled(false);
                    }


                }

            }

        }.execute();



    }



// 重写onDestroy方法用于activity退出时删除全局变量中当前聊天用户
    @Override
    protected void onDestroy() {
        super.onDestroy();

        List<Message> list=LTadpter.getLiaoTianJiLu();
        if (list.size()>0) {
            Message message = list.get(list.size() - 1);
            Intent intent = new Intent();
            intent.setAction("UpDateXiaoXiLiuLan");
            intent.putExtra("username", ConnectionAdmin.chatingname);
            intent.putExtra("neirong", message.getBody());
            sendBroadcast(intent);
        }
        ConnectionAdmin.chatingname=null;

    }
    public void top_backward(View v){
        finish();
    }
}
