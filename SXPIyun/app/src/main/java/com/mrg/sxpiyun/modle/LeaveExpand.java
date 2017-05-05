package com.mrg.sxpiyun.modle;

/**
 * Created by MrG on 2016-10-22.
 */
public class LeaveExpand {
    private String name;//名字
    private String classs;//班级
    private String reason;//假因
    private String startdata;//开始时间
    private String stopdata;//结束时间
    private String teacher;//班主任签名
    private String director;//学工办签名
    private String process;//流程
    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClasss() {
        return classs;
    }

    public void setClasss(String classs) {
        this.classs = classs;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStartdata() {
        return startdata;
    }

    public void setStartdata(String startdata) {
        this.startdata = startdata;
    }

    public String getStopdata() {
        return stopdata;
    }

    public void setStopdata(String stopdata) {
        this.stopdata = stopdata;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

}
