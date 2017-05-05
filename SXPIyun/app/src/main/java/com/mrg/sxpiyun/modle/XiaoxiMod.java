package com.mrg.sxpiyun.modle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrG on 2016-07-22.
 */
public class XiaoxiMod implements Serializable {
   //用户名
    private String title=null;

    private List<String> Lishixiaoxi=new ArrayList<String>();
    private String time;
    public XiaoxiMod(String title,String time,String newxiaoxi){
        this.title=title;
        this.time=time;
        addLishixiaoxi(newxiaoxi);

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getLishixiaoxi() {
        return Lishixiaoxi;
    }


    public void addLishixiaoxi(String newxiaoxi){
        this.Lishixiaoxi.add(newxiaoxi);
    }
}
