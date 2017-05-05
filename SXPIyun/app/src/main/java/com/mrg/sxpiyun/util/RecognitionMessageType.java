package com.mrg.sxpiyun.util;

import com.mrg.sxpiyun.modle.LeaveExpand;

import org.jivesoftware.smack.packet.Message;

/**
 * Created by MrG on 2016-11-03.
 */
public class RecognitionMessageType {

    public String getType(Message message){

        String s = message.getBody().toString();
        int i = s.indexOf("[*leave*]");
        System.out.println("判断消息类型="+i);
        if (i>=0){
            return "[*leave*]";
        }
        return "[*chat*]";


    }
    public Message getLeaveMessage(LeaveExpand leaveExpand){
        Message message = new Message();
        String name = leaveExpand.getName();
        String classs = leaveExpand.getClasss();
        String startdata = leaveExpand.getStartdata();
        String stopdata = leaveExpand.getStopdata();
        String reason = leaveExpand.getReason();
        String teacher = leaveExpand.getTeacher();
        String director = leaveExpand.getDirector();
        String process = leaveExpand.getProcess();
        /*
            [$name$]姓名
            [$class$]班级
            [$reason$]假因
            [$startdata$]开始时间
            [$stopdata$]结束时间
            [$teacher$]班主任
            [$director$]学工办

        */
        message.setBody("[*leave*][$name$]" + name + "[$class$]" + classs + "[$reason$]" + reason + "[$startdata$]" + startdata
                + "[$stopdata$]" + stopdata + "[$teacher$]" + teacher + "[$director$]" + director + "[$process$]" + process);
        return message;
    }

    /**
     * 获取名字
     * @param body
     * @return
     */
    public String getName(String body){

        return body.substring(body.indexOf("[$name$]")+8, body.indexOf("[$class$]"));
    }

    /**
     * 获取班级
     * @param body
     * @return
     */
    public String getclass(String body){

        return body.substring(body.indexOf("[$class$]")+9, body.indexOf("[$reason$]"));
    }
    /**
     * 获取假因
     * @param body
     * @return
     */
    public String getreason(String body){

        return body.substring(body.indexOf("[$reason$]")+10, body.indexOf("[$startdata$]"));
    }

    /**
     * 获取开始时间
     * @param body
     * @return
     */
    public String getstartdata(String body){

        return body.substring(body.indexOf("[$startdata$]")+13, body.indexOf("[$stopdata$]"));
    }

    /**
     * 获取结束时间
     * @param body
     * @return
     */
    public String getstopdata(String body){

        return body.substring(body.indexOf("[$stopdata$]")+12, body.indexOf("[$teacher$]"));
    }

    /**
     * 获取班主任
     * @param body
     * @return
     */
    public String getteacher(String body){

        return body.substring(body.indexOf("[$teacher$]")+11, body.indexOf("[$director$]"));
    }

    /**
     * 获取学工办
     * @param body
     * @return
     */
    public String getdirector(String body){

        return body.substring(body.indexOf("[$director$]")+12, body.indexOf("[$process$]"));
    }

    public String getprocess(String body){

        return body.substring(body.indexOf("[$process$]")+11, body.length());
    }

}
