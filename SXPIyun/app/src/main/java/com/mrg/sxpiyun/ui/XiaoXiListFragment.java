package com.mrg.sxpiyun.ui;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zxg.sxpi.ui.R;
import com.mrg.sxpiyun.adpter.XiaoXiListAdpter;
import com.mrg.sxpiyun.db.LianXiRenBD;
import com.mrg.sxpiyun.db.XiaoXiBD;
import com.mrg.sxpiyun.db.XiaoXiListBD;
import com.mrg.sxpiyun.modle.XiaoxiMod;
import com.mrg.sxpiyun.modle.ZuiXinXiaoXi;
import com.mrg.sxpiyun.util.ConnectionAdmin;
import com.mrg.sxpiyun.util.FriendInfo;
import com.mrg.sxpiyun.util.GetName;
import com.mrg.sxpiyun.util.LianxirenList;
import com.mrg.sxpiyun.util.RecognitionMessageType;
import com.mrg.sxpiyun.util.UserAdmin;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.offline.OfflineMessageManager;

import java.util.HashMap;
import java.util.List;

public class XiaoXiListFragment extends Fragment {
    private String username = ConnectionAdmin.chatingname;
    private ListView ui_xiaoxilist;
    public static XiaoXiListAdpter xiaoXiListAdpter;
    private HashMap<String, XiaoxiMod> lishixiaoxi = new HashMap<String, XiaoxiMod>();
    private   XiaoXiBD xiaoXiBD;
    private List<ZuiXinXiaoXi> zuiXinXiaoXiList;
    private  XiaoXiListBD xiaoXiListBD;
    private LianXiRenBD lianxirenBD;
    private BroadcastReceiver myRecaiver;
    private  AsyncTask<Void, Message, Void> asyncTask;
    private ProviderManager providerManager;
    private  RecognitionMessageType recognitionMessageType;
    private  FriendInfo friendInfo;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_xiao_xi_list, container, false);
        TextView textView = (TextView) view.findViewById(R.id.tv_top_title);
        textView.setText("消息");
        TextView backward = (TextView) view.findViewById(R.id.btn_top_backward);

        backward.setVisibility(View.GONE);
        TextView forward = (TextView) view.findViewById(R.id.btn_top_forward);
        forward.setVisibility(View.VISIBLE);
        forward.setBackgroundResource(R.drawable.dianxiren_ico);
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UtilActivity.class);
                intent.putExtra("type", "联系人");
                startActivity(intent);
            }
        });

        ui_xiaoxilist = (ListView) view.findViewById(R.id.lv_xiaoxilist);
        lianxirenBD = new LianXiRenBD(this.getActivity());
        xiaoXiListBD = new XiaoXiListBD(this.getActivity(), UserAdmin.username);
        xiaoXiBD = new XiaoXiBD(this.getActivity(), UserAdmin.username);
        providerManager = new ProviderManager();
        recognitionMessageType = new RecognitionMessageType();
        friendInfo = new FriendInfo();
        //查詢最新消息数据库
        zuiXinXiaoXiList = xiaoXiListBD.quertAllXiaoXi();
        //新建消息列表适配器
        xiaoXiListAdpter = new XiaoXiListAdpter(this.getActivity(), zuiXinXiaoXiList, xiaoXiListBD);
        // 装载适配器
        ui_xiaoxilist.setAdapter(xiaoXiListAdpter);
        //item点击事件处理
        ui_xiaoxilist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ZuiXinXiaoXi zxx = (ZuiXinXiaoXi) parent.getItemAtPosition(position);
                String titletext = zxx.getTitle();
                String news = zxx.getNews();
                final String name = zxx.getName();
                System.out.println("类型区别：" + name);
                if (name.indexOf("[#好友申请#]") >= 0) {
                    System.out.println("进入好友申请界面");
                    new AlertDialog.Builder(getActivity())
                            .setTitle("好友申请")
                            .setMessage("是否要添加该好友(默认添加)")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new HaoYouSouSuoActivity().addFriend(name);
                                    lianxirenBD.addUser(name);
                                    //从消息列表中移除好友申请
                                    xiaoXiListBD.removeXiaoxi(name);

                                    //广播刷新联系人列表
                                    Intent intent = new Intent();
                                    intent.setAction("ShuaXiLianXiRenList");
                                    getActivity().sendBroadcast(intent);
                                    //刷新消息列表
                                    xiaoXiListAdpter.suaxinadpter();


                                }
                            })
                            .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //从服务器中删除联系人
                                    LianxirenList.removeUser(GetName.getUsername(name));
                                    //从本地数据库中删除联系人
                                    lianxirenBD.removeUser(GetName.getName(name));
                                    //广播刷新联系人列表

                                    Intent intent = new Intent();
                                    intent.setAction("ShuaXiLianXiRenList");
                                    getActivity().sendBroadcast(intent);
                                    //从消息列表中移除好友申请
                                    xiaoXiListBD.removeXiaoxi("好友申请");
                                    //刷新消息列表
                                    xiaoXiListAdpter.suaxinadpter();


                                }
                            })
                            .show();
                } else if (name.indexOf("[#请假条#]") >= 0) {
                    System.out.println("进入请假条界面");
                    Intent intent = new Intent(getActivity(), LeaveActivity.class);
                    intent.putExtra("username", name);
                    intent.putExtra("newmsg", news);
                    startActivity(intent);

                } else {
                    System.out.println("进入聊天界面");
                    String userName = lianxirenBD.getUsername(name);

                    Intent intent = new Intent(getActivity(), LiaotianActivity.class);
                    intent.putExtra("username", userName);
                    startActivity(intent);
                }
            }
        });

        asyncTask = new AsyncTask<Void, Message, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                System.out.println("创建消息接听");
                //消息管理对象
                //获得一个所以单聊的消息监听器

                ConnectionAdmin.chatManager.addChatListener(new ChatManagerListener() {
                    @Override
                    public void chatCreated(Chat chat, boolean b) {

                        if (!b) {

                            System.out.println("获得一个新chat" + chat.getThreadID());

                            chat.addMessageListener(new MessageListener() {
                                @Override
                                public void processMessage(Chat chat, Message message) {
                                    System.out.println("来自：" + chat.getThreadID() + "消息内容:" + message.getBody());
                                    //消息类型为请假

                                    if (recognitionMessageType.getType(message).equals("[*leave*]")) {
                                        System.out.println("收到请假消息");
                                        xiaoXiBD.addLeaveMsg(message);
                                        Intent intent = new Intent();
                                        intent.setAction("shouleave");
                                        intent.putExtra("username",message.getFrom());
                                        getActivity().sendBroadcast(intent);
                                    } else if (ConnectionAdmin.chatingname != null && GetName.getName(message.getFrom()).equals(GetName.getName(ConnectionAdmin.chatingname))) {
                                        if (message.getBody() != null) {
                                            System.out.println("当前用户正在聊天");
                                            fasunmsg(message);
                                            xiaoXiBD.addNewMsg(message, "no");
                                        }
                                    } else {
                                        System.out.println("后台接收");
                                        if (message.getBody() != null) {

                                            jianshouxiaoxi(message);
                                            xiaoXiBD.addNewMsg(message);
                                        }
                                    }
                                }
                            });
                        } else {
                            System.out.println("Chat已经创建" + chat.getThreadID());

                        }

                    }
                });


                //获取离线消息
                OfflineMessageManager offlineManager = new OfflineMessageManager(ConnectionAdmin.conns);

                try {
                    //调用getmessages后会触发消息管理器，所以没必要重新导入离线消息到消息列表
                    offlineManager.getMessages();
                    //获取后删除离线消息，否则每次登陆都会重复接收离线消息
                    offlineManager.deleteMessages();
                    //接收离线消息后再发送上线
                    ConnectionAdmin.conns.sendPacket(new Presence(Presence.Type.available));
                } catch (SmackException.NoResponseException e) {
                    e.printStackTrace();
                } catch (XMPPException.XMPPErrorException e) {
                    e.printStackTrace();
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }


                return null;
            }

        };

        IntentFilter filter1 = new IntentFilter();
        filter1.addAction("HaoYouTianJia");
        filter1.addAction("UpDateXiaoXiLiuLan");
        filter1.addAction("LoginSuccess");
        filter1.addAction("shouleave");
        filter1.addAction("updataLeaveMsg");

        //注册广播
        myRecaiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("HaoYouTianJia")) {
                    Message message = new Message();
                    message.setFrom(intent.getStringExtra("username"));
                    String nichen = friendInfo.getNiCheng(intent.getStringExtra("username"));
                    message.setBody("["+nichen+"]请求添加您为好友");
                    xiaoXiListBD.addQiTaMsg(message, "好友申请");
                    xiaoXiListAdpter.suaxinadpter();
                    System.out.println("广播好友申请接收");
                } else if (intent.getAction().equals("UpDateXiaoXiLiuLan")) {
                    System.out.println("广播接收---更新消息列表");
                    String userName = intent.getStringExtra("username");
                    Message message = new Message();
                    message.setFrom(userName);
                    message.setBody(intent.getStringExtra("neirong"));
                    System.out.println("跟新消息");

                    xiaoXiListBD.removeXiaoxi(userName);
                    System.out.println("改为已读");
                    xiaoXiListBD.addNewChatMsg(message, 0, System.currentTimeMillis());


                    System.out.println("刷新界面");

                    xiaoXiListAdpter.suaxinadpter();
                } else if (intent.getAction().equals("LoginSuccess")||intent.getAction().equals("loadingwerk")) {
                    //登陆成功运行消息接收
                    asyncTask.execute();


                }else if (intent.getAction().equals("shouleave")){
                    Message message = new Message();
                    message.setFrom(intent.getStringExtra("username"));
                    Message message1 = xiaoXiBD.getLeaveMsgAll(intent.getStringExtra("username")).get(0);
                    String name = recognitionMessageType.getName(message1.getBody());
                    String getprocess = recognitionMessageType.getprocess(message1.getBody());
                    if (getprocess.equals("发送申请")){
                        message.setBody(name+" 向您请假");
                    }else if (getprocess.equals("班主任批假")){
                        message.setBody("假条审批");
                    }

                    if (xiaoXiListBD.quertIsExist(GetName.getName(message.getFrom())+"[#请假条#]")){
                        xiaoXiListBD.removeXiaoxi(GetName.getName(message.getFrom())+"[#请假条#]");
                    }
                    xiaoXiListBD.addQiTaMsg(message,"请假条");
                    xiaoXiListAdpter.suaxinadpter();
                }else if (intent.getAction().equals("updataLeaveMsg")){
                    String userName = intent.getStringExtra("username");
                    Message message = new Message();
                    message.setFrom(userName);
                    message.setBody(intent.getStringExtra("neirong"));
                    System.out.println("跟新消息");
                    xiaoXiListBD.removeXiaoxi(userName);
                    System.out.println("改为已读");
                    xiaoXiListBD.addQiTaMsg(message, "请假条");
                    xiaoXiListBD.updateNewMsgshu(userName,0);
                    System.out.println("刷新界面");
                    xiaoXiListAdpter.suaxinadpter();
                }
            }
        };
        getActivity(). registerReceiver(myRecaiver, filter1);

        return view;
    }


    //调用该方法从子线程返回到主线程
    public void fasunmsg(final Message message) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                return null;
            }

            protected void onPostExecute(Void result) {
                //回到主线程才可调用添加方法
                LiaotianActivity.LTadpter.addMessage(message);

                //  Toast.makeText(getApplicationContext(), "消息来了", Toast.LENGTH_SHORT).show();
            }
        }.execute();


    }

    //调用该方法从子线程返回到主线程
    public void jianshouxiaoxi(final Message zuixinxiaoxi) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                return null;
            }

            protected void onPostExecute(Void result) {
                //回到主线程才可调用添加方法
                xiaoXiListAdpter.addXiaoxi(zuixinxiaoxi);


            }
        }.execute();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(myRecaiver);
    }
}
