package com.mrg.sxpiyun.util;

/**
 * 全局使用的变量
 * Created by MrG on 2016-07-18.
 */
public class UserAdmin {
    //用户名
    public static String username = "";
    //密码
    public static String password = "";
    //真实姓名
    public static String truename = "";
    //真实班级
    public static String classs = "";
    //昵称
    public static String nichen = "";
    //专业
    public static String professional = "";

    public static void CloseLogin() {
        username = "";
        password = "";
        truename = "";
        classs = "";
        nichen = "";
        professional = "";


    }
}
