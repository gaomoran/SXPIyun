package com.mrg.sxpiyun.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by MrG on 2016-10-15.
 */
public class DataTimeUtil {



    public static String getFormatDataTime(long datatime){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        //现在系统时间 格式20161015220329
        String today = formatter.format(curDate);
        //现在系统日期 格式20161015
        String todayData=today.substring(0, 8);
        //现在系统时间 格式220329
        String todayTime=today.substring(8,13);

        Date data=new Date(datatime);

        String nowtime=formatter.format(data);
        String redatatime=nowtime;
        //传值的系统日期 格式20161015
        String nowtimeData=nowtime.substring(0, 8);
        //传值的系统时间 格式22:03
        String nowtimeTime=nowtime.substring(8, 10)+":"+nowtime.substring(10,12);

        if(todayData.equals(nowtimeData)){//如果时间是今天的 就返回时间

            redatatime=nowtimeTime;
        }else {//如果不是今天的 就返回日期
            //格式 2016-10-15
            redatatime=nowtimeData.substring(0,4)+"-"+nowtimeData.substring(4,6)+"-"+nowtimeData.substring(6,8);
        }

        return redatatime;
    }
}
