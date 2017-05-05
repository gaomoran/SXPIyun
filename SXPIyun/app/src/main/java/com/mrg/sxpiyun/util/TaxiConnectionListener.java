package com.mrg.sxpiyun.util;

import android.util.Log;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by MrG on 2016-10-21.
 */
public class TaxiConnectionListener implements ConnectionListener {
    private Timer tExit;
    private String username;
    private String password;
    private int logintime = 2000;
    private Connent connent;

    @Override
    public void connected(XMPPConnection xmppConnection) {

    }

    @Override
    public void authenticated(XMPPConnection xmppConnection) {

    }

    @Override
    public void connectionClosed() {
        Log.i("TaxiConnectionListener", "連接關閉");
        // 關閉連接
        try {
            ConnectionAdmin.conns.disconnect();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        // 重连服务器
        tExit = new Timer();
        tExit.schedule(new timetask(), logintime);
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        Log.i("TaxiConnectionListener", "連接關閉異常");
        // 判斷為帳號已被登錄
        boolean error = e.getMessage().equals("stream:error (conflict)");
        if (!error) {
            // 關閉連接
          try {
                ConnectionAdmin.conns.disconnect();
            } catch (SmackException.NotConnectedException e2) {
                e2.printStackTrace();
            }
            // 重连服务器
            tExit = new Timer();
            tExit.schedule(new timetask(), logintime);

        }
    }

    class timetask extends TimerTask {
        @Override
        public void run() {
            username =UserAdmin.username;
            password = UserAdmin.password;
            connent = new Connent();
            if (connent.onConnet()) {
                if (username != null && password != null) {
                    Log.i("TaxiConnectionListener", "嘗試登錄");
                    // 连接服务器
                    if (connent.onLogin(username, password)) {
                        Log.i("TaxiConnectionListener", "登錄成功");
                    } else {
                        Log.i("TaxiConnectionListener", "重新登錄");
                    }
                }
            }else {
                System.out.println("重新连接服务器");
            }
        }
    }

    @Override
    public void reconnectingIn(int arg0) {
    }

    @Override
    public void reconnectionFailed(Exception arg0) {
        tExit.schedule(new timetask(), logintime);

    }

    @Override
    public void reconnectionSuccessful() {
        System.out.println("重连成功");
    }

}