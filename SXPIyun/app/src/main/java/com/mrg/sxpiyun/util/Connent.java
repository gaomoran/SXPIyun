package com.mrg.sxpiyun.util;

import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MrG on 2016-10-21.
 */
public class Connent {

    private XMPPConnection connection;

    /**
     * 连接到服务器
     * @return
     */
    public boolean onConnet(){
        ConnectionAdmin.connsip="123.56.222.103";
        //配置参数对象
        ConnectionConfiguration config= new ConnectionConfiguration(ConnectionAdmin.connsip,5222);
        //是否开启安全模式
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        //允许重连
        config.setReconnectionAllowed(true);
        //开启调试模式
        config.setDebuggerEnabled(true);
        //设置状态
        config.setSendPresence(false);
        //开启sasl认证机制
        SASLAuthentication.supportSASLMechanism("PLAIN");
        //连接对象
        connection = new XMPPTCPConnection(config);
        //连接
        try {
            connection.connect();
        } catch (Exception e) {
            return false;
        }
        ConnectionAdmin.conns=connection;
        return true;
    }

    /**
     * 登陆
     * @param username
     * @param password
     * @return
     */
    public boolean onLogin(String username,String password){
        try {
            ConnectionAdmin.conns.login(username,password);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        //记录连接已经登陆的对象
        ConnectionAdmin.conns=connection;
        //记录消息管理器
        ConnectionAdmin.chatManager= ChatManager.getInstanceFor(ConnectionAdmin.conns);
        Roster roster = ConnectionAdmin.conns.getRoster();
        //获得联系人
        ConnectionAdmin.roster=roster;
        //设置连接接听
        ConnectionAdmin.conns.addConnectionListener(ConnectionAdmin.listener);
        UserAdmin.username=username;
        UserAdmin.password=password;
        return true;
    }

    /**
     * 注册
     *
     * @param username
     *            注册帐号
     * @param password
     *            注册密码
     * @return 1、注册成功 0、服务器没有返回结果2、这个账号已经存在3、注册失败
     */
    public String regist(String username, String password) {

        Registration reg = new Registration();
        reg.setType(IQ.Type.SET);
        Map<String, String> attributes = new HashMap<String, String>();
        reg.setTo(connection.getHost());
        attributes.put("username", username);
        attributes.put("password", password);
        reg.setAttributes(attributes);
        PacketFilter filter = new AndFilter(new PacketIDFilter(reg.getPacketID()), new PacketTypeFilter(IQ.class));
        PacketCollector collector = connection.createPacketCollector(filter);
        try {
            connection.sendPacket(reg);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        IQ result = (IQ) collector.nextResult(SmackConfiguration.getDefaultPacketReplyTimeout());
        // 停止从队列中等待
        collector.cancel();


        if (result == null) {
          System.out.println("服务器没有返回结果");
        }
        else if (result.getType() == IQ.Type.ERROR) {
            System.out.println("数据包"+result.toXML().toString());
            if (result.toXML().toString().indexOf("cancel")>-1){
                return "这个账号已经存在";
            }else{
                return "注册失败";
            }
        }else if (result.getType() == IQ.Type.RESULT){
            return "恭喜你注册成功";
        }
        return "未知错误";
    }

}
