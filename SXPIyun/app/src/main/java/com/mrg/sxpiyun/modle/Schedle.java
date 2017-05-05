package com.mrg.sxpiyun.modle;

import java.io.Serializable;

/**
 * Created by MrG on 2016-11-16.
 */
public class Schedle implements Serializable {
    private String courses;//课程
    private String teacher;//教师
    private String location;//地点
    private int week;//星期
    private int No;//第几节

    public String getCourses() {
        return courses;
    }

    public void setCourses(String courses) {
        this.courses = courses;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getNo() {
        return No;
    }

    public void setNo(int no) {
        No = no;

    }

    public void setCTL(String number) {
        courses = number.substring(0, number.indexOf("<br>"));
        number = number.substring(number.indexOf("<br>") + 4, number.length());
        number = number.substring(number.indexOf("<br>") + 4, number.length());
        teacher = number.substring(0, number.indexOf("["));
        location = number.substring(number.indexOf("<br>") + 4, number.length());
//        System.out.println("课程：" + courses + "老师：" + teacher + "地点：" + location);
    }
}

