package com.mrg.sxpiyun.util;

import android.graphics.Bitmap;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;

import java.io.ByteArrayOutputStream;

/**
 * Created by MrG on 2016-07-18.
 */
public class FriendInfo {


    /**
     * 获取好友昵称
     */
    public String getNiCheng(String username) {
        VCard vcard = new VCard();

        try {

            vcard.load(ConnectionAdmin.conns, GetName.getUsername(username));

        } catch (Exception e) {
            System.out.println(username + "-------没接上-----");
            e.printStackTrace();
            return username;

        }

        return vcard.getNickName();
    }

    /**
     * 修改昵称
     */
    public boolean setNiCheng(String newnicheng) {
        VCard vcard = new VCard();
        try {

            vcard.load(ConnectionAdmin.conns);

        } catch (Exception e) {
            System.out.println("-------没接上-----修改失败");
            e.printStackTrace();
            return false;
        }

        vcard.setNickName(newnicheng);
        try {
            vcard.save(ConnectionAdmin.conns);
            return true;
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 获取好友状态
     *
     * @param username
     * @return
     */
    public String getZhuangTai(String username) {
        //针对无网络状态
        Presence presence = null;
        try {
            presence = ConnectionAdmin.roster.getPresence(GetName.getUsername(username));

        } catch (Exception e) {
            return "[离线]";
        }


        String zhuangtai = presence.toString();

        if (zhuangtai.indexOf("(") != -1) {
            String zt = zhuangtai.substring(zhuangtai.indexOf("(") + 1, zhuangtai.indexOf(")"));
            return "[" + zt + "]";
        } else if (presence.isAvailable()) {
            return "[在线]";
        } else {
            return "[离线]";
        }

    }

    /**
     * 设置上传头像
     *
     * @param touxiang 头像bitmap
     * @return 是否成功 成功返回true
     */
    public boolean setTouXiang(Bitmap touxiang) {

        VCard vCard = new VCard();

        try {
            vCard.load(ConnectionAdmin.conns);

            vCard.setAvatar(Bitmap2Bytes(touxiang));
            vCard.save(ConnectionAdmin.conns);
            return true;
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }


        return false;
    }

    /**
     * 上传名字
     *
     * @param name
     * @return
     */
    public boolean setTureName(String name) {
        VCard vCard = new VCard();
        try {
            vCard.load(ConnectionAdmin.conns);
            vCard.setFirstName(name);
            vCard.save(ConnectionAdmin.conns);
            return true;
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;

    }

    /**
     * 获取真实姓名
     *
     * @return
     */
    public String getTureName() {
        VCard vCard = new VCard();
        try {
            vCard.load(ConnectionAdmin.conns);
            return vCard.getFirstName();
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        return "";

    }

    /**
     * 上传班级
     *
     * @param classs
     * @return
     */
    public boolean setTureClass(String classs) {
        VCard vCard = new VCard();
        try {
            vCard.load(ConnectionAdmin.conns);
            vCard.setLastName(classs);
            vCard.save(ConnectionAdmin.conns);
            return true;
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * 获取班级
     *
     * @return
     */
    public String getTureClass() {

        VCard vCard = new VCard();
        try {
            vCard.load(ConnectionAdmin.conns);
            return vCard.getLastName();
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        return "";

    }

    /**
     * 上传专业
     *
     * @return
     */
    public boolean setProfessional(String professional) {

        VCard vCard = new VCard();
        try {
            vCard.load(ConnectionAdmin.conns);
            vCard.setOrganization(professional);
            vCard.save(ConnectionAdmin.conns);
            return true;
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * 获取专业
     *
     * @return
     */
    public String getProfessional() {

        VCard vCard = new VCard();
        try {
            vCard.load(ConnectionAdmin.conns);
            return vCard.getOrganization();
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        return "";

    }


    /**
     * 将bitmap->byte[]
     *
     * @param bm
     * @return
     */
    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }


}