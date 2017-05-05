package com.mrg.sxpiyun.util;

/**
 * Created by MrG on 2016-07-22.
 */
public class GetName {

    public static String getName(String username) {

        try {
            if (username.indexOf("[") >= 0) {
                username = username.substring(0, username.indexOf("[#"));
            }

            if (username.indexOf("@") >= 0) {
                username = username.substring(0, username.indexOf("@"));
            }
            return username;

        }catch (Exception e){
            return "";
        }

    }

    public static String getUsername(String username) {
        String name = username;
        if (name.indexOf("[") >= 0) {
            name = name.substring(0, username.indexOf("[#"));
        }
        if (name.indexOf("@") >= 0) {
            name = name.substring(0, username.indexOf("@"));
        }
        if (name.indexOf("/") >= 0) {
            name = name.substring(0, username.indexOf("/"));
        }
        System.out.println("Getusername:" + name);
        return name + "@" + ConnectionAdmin.servicename;


    }
}
