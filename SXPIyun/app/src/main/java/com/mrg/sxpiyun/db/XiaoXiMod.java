package com.mrg.sxpiyun.db;

/**
 * Created by MrG on 2016-07-27.
 */
public class XiaoXiMod {
    //消息表，字段包含（id（主键自动添加），来自谁，发给谁，消息内容，时间,是否为新消息）
    //来自谁
    private String fromname;
    //发给谁
    private String toname;
    //消息内容
    private String number;
    //时间日期
    private long datatime;
    //是否为最新消息（yse 为最新消息，no 为旧消息）
    private String isnewmsg;


    public String getFromname() {
        return fromname;
    }

    public void setFromname(String fromname) {
        this.fromname = fromname;
    }

    public String getToname() {
        return toname;
    }

    public void setToname(String toname) {
        this.toname = toname;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }



    public long getDatatime() {
        return datatime;
    }

    public void setDatatime(long datatime) {
        this.datatime = datatime;
    }

    public String getIsnewmsg() {
        return isnewmsg;
    }

    public void setIsnewmsg(String isnewmsg) {
        this.isnewmsg = isnewmsg;
    }
}
