package com.mrg.sxpiyun.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;


import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.zxg.sxpi.ui.R;
import com.mrg.sxpiyun.adpter.MainAdpter;
import com.mrg.sxpiyun.db.XiaoXiListBD;
import com.mrg.sxpiyun.db.LianXiRenBD;
import com.mrg.sxpiyun.util.AppConfig;
import com.mrg.sxpiyun.util.ConnectionAdmin;
import com.mrg.sxpiyun.util.Connent;
import com.mrg.sxpiyun.util.FriendInfo;
import com.mrg.sxpiyun.util.GetName;
import com.mrg.sxpiyun.util.IsLogin;
import com.mrg.sxpiyun.util.LianxirenList;
import com.mrg.sxpiyun.util.TouXiangGet;
import com.mrg.sxpiyun.util.UserAdmin;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends FragmentActivity implements BottomNavigationBar.OnTabSelectedListener {
    private ViewPager viewpager;
    private MainAdpter mMianadpter;
    private TouXiangGet touXiangGet = new TouXiangGet();
    private LianXiRenBD LianXiRenBD;
    private XiaoXiListBD xiaoXiListBD;
    private PacketFilter filter;
    private PacketListener listener;
    private BroadcastReceiver myRecaiver;
    private Thread downroster;
    private PacketListener packetListener;
    private Timer timer;
    private TimerTask timerTask;
    private ArrayList<Fragment> fragments;
    public Button btn_backward;
    private FriendInfo friendInfo;

    private static Boolean isExit = false;
    private BroadcastReceiver myRecaiver1;
    private int werk=0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //底部栏相关设置
        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC
                );

        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.message, "消息").setActiveColorResource(R.color.orange))
                .addItem(new BottomNavigationItem(R.drawable.kebiao, "课表").setActiveColorResource(R.color.teal))
                .addItem(new BottomNavigationItem(R.drawable.gongju, "工具").setActiveColorResource(R.color.blue))
                .addItem(new BottomNavigationItem(R.drawable.user, "我").setActiveColorResource(R.color.brown))
                .setFirstSelectedPosition(0)
                .initialise();
        bottomNavigationBar.setTabSelectedListener(this);
        fragments = getFragments();
        setDefaultFragment();


        //使用工具实例化
        friendInfo = new FriendInfo();
        LianXiRenBD = new LianXiRenBD(getApplicationContext());
        xiaoXiListBD = new XiaoXiListBD(getApplicationContext(), UserAdmin.username);

        //注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("LoginSuccess");
        this.myRecaiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("LoginSuccess")) {
                    System.out.println("收到广播开始加载网络项目");
                    werk=1;
                    downroster.start();
                    ConnectionAdmin.conns.addPacketListener(packetListener, null);
                }

            }
        };
        registerReceiver(this.myRecaiver, filter);

        //延时任务
        timer = new Timer();
        timerTask = new TimerTask() {

            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setAction("LoginSuccess");
                sendBroadcast(intent);
                System.out.println("直接发送广播");
            }
        };
        //自动登陆
        Thread login_connent = new Thread() {
            public void run() {
                Connent connent = new Connent();
                if (connent.onConnet()) {
                    if (connent.onLogin(UserAdmin.username, UserAdmin.password)) {
                        timer.schedule(timerTask, 1100);

                    }
                } else {
                    System.out.println("登陆失败");
                    IsLogin.islogin = false;
                }
            }
        };
        //判断是否登陆
        if (IsLogin.islogin == false) {
            login_connent.start();
        } else {
            //通过登陆界面登陆
            Intent regdata = getIntent();
            String nichen = regdata.getStringExtra("nichen");
            if (nichen!=null) {
                friendInfo.setNiCheng(nichen);
            }
            timer.schedule(timerTask, 1100);

        }

        //异步加载联系人到本地数据库
        downroster = new Thread() {

            public void run() {

                XMPPConnection conn = ConnectionAdmin.conns;
                Roster roster = conn.getRoster();

                ConnectionAdmin.roster = roster;
                UserAdmin.nichen = friendInfo.getNiCheng(UserAdmin.username);

                //下载所有联系人到本地数据库
                LianXiRenBD.setLianxirenAll(roster);
                //获取我的头像
                touXiangGet.getTouxiang(UserAdmin.username, getApplicationContext());
            }


        };

        //数据包处理中心
        packetListener = new PacketListener() {
            @Override
            public void processPacket(Packet packet) throws SmackException.NotConnectedException {

                System.out.println("收到数据包");
                //Presence包处理中心
                if (packet instanceof Presence) {
                    Presence presence = (Presence) packet;
                    String from = presence.getFrom();//发送方
                    String to = presence.getTo();//接收方
                    if (presence.getType().equals(Presence.Type.subscribe)) {
                        System.out.println("收到添加请求！");
                        Intent intent = new Intent();
                        intent.setAction("HaoYouTianJia");

                        intent.putExtra("username", presence.getFrom());
                        sendBroadcast(intent);
                        System.out.println("发送好友申请广播");


                    } else if (presence.getType().equals(
                            Presence.Type.subscribed)) {
                        LianXiRenBD.addUser(presence.getFrom().toString());
                        System.out.println("恭喜，对方同意添加好友！");

                    } else if (presence.getType().equals(
                            Presence.Type.unsubscribe)) {
                        System.out.println("抱歉，对方拒绝添加好友，将你从好友列表移除！");
                        //从本地数据库中删除好友
                        LianXiRenBD.removeUser(GetName.getName(presence.getFrom()));
                        //从服务器删除好友
                        LianxirenList.removeUser(GetName.getUsername(presence.getFrom()));
                        //广播刷新联系人列表
                        Intent intent = new Intent();
                        intent.setAction("ShuaXiLianXiRenList");
                        sendBroadcast(intent);


                    } else if (presence.getType().equals(
                            Presence.Type.unsubscribed)) {
                    } else if (presence.getType().equals(
                            Presence.Type.unavailable)) {
                        System.out.println("好友下线！");
                    } else {
                        System.out.println("好友上线！");
                    }
                }
            }
        };


    }

    /**
     * 底部栏辅助类用于替换fragment
     */
    private void setDefaultFragment() {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.main_content, new XiaoXiListFragment());
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //退出时注销广播
        unregisterReceiver(myRecaiver);
    }

    /**
     * 底部栏类方法
     * @param position
     */
    @Override
    public void onTabSelected(int position) {

        if (fragments != null) {
            if (position < fragments.size()) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = fragments.get(position);
                if (fragment.isAdded()) {
                    ft.replace(R.id.main_content, fragment);
                } else {
                    ft.add(R.id.main_content, fragment,position+"");
                }
                ft.commitAllowingStateLoss();
            }
        }
    }

    /**
     * 底部栏类方法
     * @param position
     */
    @Override
    public void onTabUnselected(int position) {
        if (fragments != null) {
            if (position < fragments.size()) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = fragments.get(position);
                ft.remove(fragment);
                ft.commitAllowingStateLoss();
            }
        }
    }

    /**
     * 底部栏类方法
     * @param position
     */
    @Override
    public void onTabReselected(int position) {

    }

    /**
     * 加载所有fragment到列表
     * @return
     */
    public ArrayList<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new XiaoXiListFragment());
        fragments.add(new ScheduleFragment());
        fragments.add(new UtilFragment());
        fragments.add(new MyFragment());
        return fragments;
    }

    /**
     * 注消登陆
     *
     * @param v
     */
    public void Myclose(View v) {

        AppConfig appConfig = new AppConfig(this);
        appConfig.Closelogin();
        try {
            ConnectionAdmin.conns.sendPacket(new Presence(Presence.Type.unavailable));
            ConnectionAdmin.conns.removeConnectionListener(ConnectionAdmin.listener);
            ConnectionAdmin.conns.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ConnectionAdmin.Closelogin();
        UserAdmin.CloseLogin();
        String PATH = Environment.getExternalStorageDirectory().toString();
        File ff = new File(PATH + "/SXPIyun/kebiao/" + UserAdmin.username + ".html");
        if (ff.exists()) {
            ff.delete();
        }

        Intent intent = new Intent(this, TestLoginActivity.class);
        startActivity(intent);
        finish();

    }

    /**
     * 按键接听
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //退出键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Timer tExit = null;
            if (isExit == false) {
                isExit = true; // 准备退出
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                tExit = new Timer();
                tExit.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        isExit = false; // 取消退出
                    }
                }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

            } else {
                finish();
                System.exit(0);
            }
        }


        return true;
    }

    /**
     * 后台时不保存fragment，防止重叠
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }
}
