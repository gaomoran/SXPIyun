package com.mrg.sxpiyun.modle;

import java.io.Serializable;

/**
 * Created by MrG on 2016-12-12.
 */
public class Results implements Serializable {

    private String xuenian;//学年
    private String xueqi;//学期
    private String kechengming;//课程名
    private String kechengtype;//考核类型
    private String teacher;//任课教师
    private String kaohefangshi;//考核方式
    private String zongping;//总评
    private String bukao;//补考成绩
    private String chongxiu;//重修成绩
    private String yingdexuefeng;//应得学分
    private String xuefengjidian;//学分绩点

    public String getXuenian() {
        return xuenian;
    }

    public void setXuenian(String xuenian) {
        this.xuenian = xuenian;
    }

    public String getXueqi() {
        return xueqi;
    }

    public void setXueqi(String xueqi) {
        this.xueqi = xueqi;
    }

    public String getKechengming() {
        return kechengming;
    }

    public void setKechengming(String kechengming) {
        this.kechengming = kechengming;
    }

    public String getKechengtype() {
        return kechengtype;
    }

    public void setKechengtype(String kechengtype) {
        this.kechengtype = kechengtype;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getKaohefangshi() {
        return kaohefangshi;
    }

    public void setKaohefangshi(String kaohefangshi) {
        this.kaohefangshi = kaohefangshi;
    }

    public String getZongping() {
        return zongping;
    }

    public void setZongping(String zongping) {
        this.zongping = zongping;
    }

    public String getBukao() {
        return bukao;
    }

    public void setBukao(String bukao) {
        this.bukao = bukao;
    }

    public String getChongxiu() {
        return chongxiu;
    }

    public void setChongxiu(String chongxiu) {
        this.chongxiu = chongxiu;
    }

    public String getYingdexuefeng() {
        return yingdexuefeng;
    }

    public void setYingdexuefeng(String yingdexuefeng) {
        this.yingdexuefeng = yingdexuefeng;
    }

    public String getXuefengjidian() {
        return xuefengjidian;
    }

    public void setXuefengjidian(String xuefengjidian) {
        this.xuefengjidian = xuefengjidian;
    }



}
