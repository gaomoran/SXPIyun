package com.mrg.sxpiyun.ui;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zxg.sxpi.ui.R;
import com.mrg.sxpiyun.db.XiaoXiBD;
import com.mrg.sxpiyun.modle.LeaveExpand;
import com.mrg.sxpiyun.util.ConnectionAdmin;
import com.mrg.sxpiyun.util.GetName;
import com.mrg.sxpiyun.util.RecognitionMessageType;
import com.mrg.sxpiyun.util.UserAdmin;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Message;

import java.util.List;

public class LeaveActivity extends FragmentActivity {

    private LeaveFragment leaveFragment;
    private FragmentTransaction transaction;
    private FragmentManager supportFragmentManager;
    private XiaoXiBD xiaoXiBD;
    private List<Message> leaveList;
    private Message message;
    private LinearLayout ll_teacheryn;
    private LinearLayout ll_directoryn;
    private TextView tv_teacheryn;
    private TextView tv_directoryn;
    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave);
        Intent intent = getIntent();
        supportFragmentManager = getSupportFragmentManager();
        transaction = supportFragmentManager.beginTransaction();
        leaveFragment = new LeaveFragment();
        transaction.add(R.id.framelayout_leave, leaveFragment, "leave");
        transaction.commit();
        xiaoXiBD = new XiaoXiBD(this, UserAdmin.username);
        leaveList = xiaoXiBD.getLeaveMsgAll(intent.getStringExtra("username"));
        flag=intent.getStringExtra("newmsg");

    }

    @Override
    protected void onStart() {
        super.onStart();
        View view = supportFragmentManager.findFragmentByTag("leave").getView();
        Button btn_update = (Button) view.findViewById(R.id.btn_leave_updata);
        LinearLayout ll_director = (LinearLayout) view.findViewById(R.id.ll_leave_director);
        LinearLayout ll_teacher = (LinearLayout) view.findViewById(R.id.ll_leave_teacher);
        ll_teacheryn = (LinearLayout) view.findViewById(R.id.ll_leave_teacheryn);
        ll_directoryn = (LinearLayout) view.findViewById(R.id.ll_leave_directoryn);
        EditText et_name = (EditText) view.findViewById(R.id.et_leave_name);
        EditText et_class = (EditText) view.findViewById(R.id.et_leave_class);
        EditText et_reason = (EditText) view.findViewById(R.id.et_leave_reason);
        tv_teacheryn = (TextView) view.findViewById(R.id.tv_leave_teacheryn);
        tv_directoryn = (TextView) view.findViewById(R.id.tv_leave_directoryn);
        TextView tv_startdata = (TextView) view.findViewById(R.id.tv_leave_startdata);
        TextView tv_stopdata = (TextView) view.findViewById(R.id.tv_leave_stopdata);
        DatePicker et_startdata = (DatePicker) view.findViewById(R.id.dp_leave_startdata);
        DatePicker et_stopdata = (DatePicker) view.findViewById(R.id.dp_leave_stopdata);
        message = leaveList.get(0);
        String body = message.getBody();
        RecognitionMessageType recognitionMessageType = new RecognitionMessageType();
        et_name.setText(recognitionMessageType.getName(body));
        et_class.setText(recognitionMessageType.getclass(body));
        et_reason.setText(recognitionMessageType.getreason(body));
        tv_startdata.setText(recognitionMessageType.getstartdata(body));
        tv_stopdata.setText(recognitionMessageType.getstopdata(body));

        String getteacher = recognitionMessageType.getteacher(body);
        if (!getteacher.equals("申请中")){
            ll_teacheryn.setVisibility(View.GONE);
            tv_teacheryn.setVisibility(View.VISIBLE);
            tv_teacheryn.setText(getteacher);

        }
        ll_teacher.setVisibility(View.VISIBLE);
//        ll_director.setVisibility(View.VISIBLE);
        btn_update.setVisibility(View.GONE);
        et_startdata.setVisibility(View.GONE);
        et_stopdata.setVisibility(View.GONE);
        tv_startdata.setVisibility(View.VISIBLE);
        tv_stopdata.setVisibility(View.VISIBLE);
        System.out.println("改变请假布局");

    }
    /**
     * 班主任同意
     * @param v
     */
    public void teacherYN(View v){
        LeaveExpand leaveExpand = new LeaveExpand();
        RecognitionMessageType recognitionMessageType = new RecognitionMessageType();
        String body = message.getBody();
        leaveExpand.setName(recognitionMessageType.getName(body));
        leaveExpand.setClasss(recognitionMessageType.getclass(body));
        leaveExpand.setReason(recognitionMessageType.getreason(body));
        leaveExpand.setStartdata(recognitionMessageType.getstartdata(body));
        leaveExpand.setStopdata(recognitionMessageType.getstopdata(body));
        leaveExpand.setProcess("班主任批假");
        if (v.getId()==R.id.btn_leave_teacher_yes){
            System.out.println("班主任同意");
            leaveExpand.setTeacher("同意！");
            tv_teacheryn.setText("同意！");
            tv_teacheryn.setVisibility(View.VISIBLE);
        }else {
            System.out.println("班主任不同意");
            leaveExpand.setTeacher("不同意！");
            tv_teacheryn.setText("不同意！");
            tv_teacheryn.setVisibility(View.VISIBLE);
        }
        Message leaveMessage = recognitionMessageType.getLeaveMessage(leaveExpand);
        try {

            Chat chat = ConnectionAdmin.chatManager.createChat(GetName.getUsername(message.getFrom()), null);
            chat.sendMessage(leaveMessage);
            xiaoXiBD.removeLeaveMsg(message.getFrom());
            leaveMessage.setFrom(message.getFrom());
            xiaoXiBD.addLeaveMsg(leaveMessage);
            ll_teacheryn.setVisibility(View.GONE);
            Toast.makeText(this, "提交成功", Toast.LENGTH_SHORT).show();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
            Toast.makeText(this, "提交失败", Toast.LENGTH_SHORT).show();
        }
        flag = "您已批阅";


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Intent intent = new Intent();
        intent.putExtra("username", GetName.getName(message.getFrom()) + "[#请假条#]");
        intent.putExtra("neirong",flag);
        intent.setAction("updataLeaveMsg");
        sendBroadcast(intent);
    }
    public void top_backward(View view){
        finish();
    }
}
