package com.mrg.sxpiyun.util;

import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;

/**
 * Created by MrG on 2016-07-18.
 */
public class ConnectionAdmin {
    //和服务器的连接
    public static XMPPConnection conns= null;
    //通过连接获取的联系人
    public static Roster roster=null;
    //聊天管理对象
    public static ChatManager chatManager=null;
    //当前正在聊天的用户名
    public static String chatingname=null;
    //服务器的ip地址
    public static String connsip=null;

    public static String servicename=null;

    public static TaxiConnectionListener listener=new TaxiConnectionListener();

    public static void Closelogin(){

        conns= null;
        roster=null;
        chatManager=null;
        chatingname=null;
        servicename=null;
    }

}
