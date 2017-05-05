package com.mrg.sxpiyun.modle;

/**
 * Created by MrG on 2016-07-22.
 */
public class ZuiXinXiaoXi {
    private String name;
    private String news;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;
    private long data;
    private int msgshu;

    public ZuiXinXiaoXi(){
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }

    public int getMsgshu() {
        return msgshu;
    }

    public void setMsgshu(int msgshu) {
        this.msgshu = msgshu;
    }
}
