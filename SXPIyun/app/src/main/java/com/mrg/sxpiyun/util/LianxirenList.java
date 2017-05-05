package com.mrg.sxpiyun.util;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MrG on 2016-07-19.
 */
public class LianxirenList {


    //获取联系人分组
    public static List<RosterGroup> getLxrGroup(Roster roster){
        List<RosterGroup> lxrGroup=null;
        Collection<RosterGroup> all= roster.getGroups();
        lxrGroup= new ArrayList<RosterGroup>(all);

        return lxrGroup;
    }

    //获取联系人详细列表
    public static HashMap<String,List<RosterEntry>> getLianxiren(Roster roster) {

        HashMap<String, List<RosterEntry>> lianxiren = new HashMap<String, List<RosterEntry>>();
        List<RosterGroup> lxrGroup=getLxrGroup(roster);
        for(RosterGroup r:lxrGroup){
            Collection<RosterEntry> xx= r.getEntries();
            lianxiren.put(r.getName(),new ArrayList<RosterEntry>(xx));
        }
        return lianxiren;
    }
    /**
     * 删除好友
     */
    public static boolean removeUser(String userName) {
        System.out.println("正在删除好友...");
        try {
            RosterEntry entry=ConnectionAdmin.conns.getRoster().getEntry(userName);
            System.out.println("删除好友：" + userName);
            ConnectionAdmin.conns.getRoster().removeEntry(entry);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



}
