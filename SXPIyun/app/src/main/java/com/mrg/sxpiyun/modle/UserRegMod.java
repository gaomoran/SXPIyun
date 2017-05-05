package com.mrg.sxpiyun.modle;

/**
 * Created by MrG on 2016-06-26.
 */
public class UserRegMod {
    //学号或学工号
    private String xuehao;
    //姓名
    private String name;
    //昵称
    private String nicheng;
    //密码
    private String password;
    //邮箱
    private String email;

    public String getZhiwu() {
        return zhiwu;
    }

    public void setZhiwu(String zhiwu) {
        this.zhiwu = zhiwu;
    }

    //职务
    private String zhiwu;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNicheng() {
        return nicheng;
    }

    public void setNicheng(String nicheng) {
        this.nicheng = nicheng;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getXuehao() {
        return xuehao;
    }

    public void setXuehao(String xuehao) {
        this.xuehao = xuehao;
    }
}
