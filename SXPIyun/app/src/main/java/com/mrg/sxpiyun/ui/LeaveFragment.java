package com.mrg.sxpiyun.ui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zxg.sxpi.ui.R;
import com.mrg.sxpiyun.db.LianXiRenBD;
import com.mrg.sxpiyun.modle.LeaveExpand;
import com.mrg.sxpiyun.util.ConnectionAdmin;
import com.mrg.sxpiyun.util.GetName;
import com.mrg.sxpiyun.util.RecognitionMessageType;
import com.mrg.sxpiyun.util.UserAdmin;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Message;

public class LeaveFragment extends Fragment {

    private EditText et_name;
    private EditText et_class;
    private EditText et_reason;
    private TextView et_teacher;
    private TextView et_director;
    private Button btn_updat;
    private DatePicker et_startdata;
    private DatePicker et_stopdata;
    private int LXRNICHENG = 1;
    private LianXiRenBD lianXiRenBD;
    private LeaveExpand leaveExpand;
    private RecognitionMessageType recognitionMessageType;
    private Button btn_teacher_yes;
    private Button btn_teacher_no;
    private Button btn_director_yes;
    private Button btn_director_no;
    private LinearLayout ll_teacher;
    private LinearLayout ll_director;
    public View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_leave_framget, container, false);
        TextView textView = (TextView) view.findViewById(R.id.tv_top_title);
        textView.setText("请假条");
        Intent intent = new Intent();
        intent.setAction("XianShiTop");
        getActivity().sendBroadcast(intent);
        et_name = (EditText) view.findViewById(R.id.et_leave_name);
        et_class = (EditText) view.findViewById(R.id.et_leave_class);
        et_reason = (EditText) view.findViewById(R.id.et_leave_reason);
        et_startdata = (DatePicker) view.findViewById(R.id.dp_leave_startdata);
        et_stopdata = (DatePicker) view.findViewById(R.id.dp_leave_stopdata);
        btn_updat = (Button) view.findViewById(R.id.btn_leave_updata);
        btn_teacher_yes = (Button) view.findViewById(R.id.btn_leave_teacher_yes);
        btn_teacher_no = (Button) view.findViewById(R.id.btn_leave_teacher_no);
        btn_director_yes = (Button) view.findViewById(R.id.btn_leave_director_yes);
        btn_director_no = (Button) view.findViewById(R.id.btn_leave_director_no);
        ll_teacher = (LinearLayout) view.findViewById(R.id.ll_leave_teacher);
        ll_director = (LinearLayout) view.findViewById(R.id.ll_leave_director);
        leaveExpand = new LeaveExpand();
        recognitionMessageType = new RecognitionMessageType();
        btn_updat.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if (iftrue()) {
                    leaveExpand.setName(et_name.getText().toString());
                    leaveExpand.setClasss(et_class.getText().toString());
                    leaveExpand.setReason(et_reason.getText().toString());
                    leaveExpand.setTeacher("申请中");
                    leaveExpand.setDirector("申请中");
                    leaveExpand.setProcess("发送申请");
                    Intent intent = new Intent(getActivity(), SelectContactActivity.class);
                    startActivityForResult(intent, LXRNICHENG);
                }
            }
        });
        et_name.setText(UserAdmin.truename);
        et_class.setText(UserAdmin.classs);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LXRNICHENG && resultCode == 1) {
            String nichen = data.getStringExtra("nichen");
            lianXiRenBD = new LianXiRenBD(getActivity());
            String username = lianXiRenBD.getUsername(nichen);
            Message leaveMessage = recognitionMessageType.getLeaveMessage(leaveExpand);
            System.out.println("请假消息发给：" + GetName.getUsername(username));

            try {

                    Chat chat = ConnectionAdmin.chatManager.createChat(GetName.getUsername(username), null);
                    chat.sendMessage(leaveMessage);
                    Toast.makeText(getActivity(), "提交成功", Toast.LENGTH_SHORT).show();

            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "提交失败", Toast.LENGTH_SHORT).show();
            }catch (NullPointerException w){
                Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
            }


        }
    }

    /**
     * 判断表单正确性
     *
     * @return
     */
    private boolean iftrue() {

        String reason = et_reason.getText().toString();
        int year = et_startdata.getYear();
        int month = et_startdata.getMonth();
        int dayOfMonth = et_startdata.getDayOfMonth();
        int year1 = et_stopdata.getYear();
        int month1 = et_stopdata.getMonth();
        int dayOfMonth1 = et_stopdata.getDayOfMonth();

        if (reason.equals("")) {
            Toast.makeText(getActivity(), "假因不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (year > year1) {

            Toast.makeText(getActivity(), "日期有问题", Toast.LENGTH_SHORT).show();
            return false;
        } else if (year == year1 && month > month1) {
            Toast.makeText(getActivity(), "日期有问题", Toast.LENGTH_SHORT).show();
            return false;
        } else if (year == year1 && month == month1 && dayOfMonth > dayOfMonth1) {
            Toast.makeText(getActivity(), "日期有问题", Toast.LENGTH_SHORT).show();
            return false;
        }
        leaveExpand.setStartdata(year + "年" + month + "月" + dayOfMonth + "日");
        leaveExpand.setStopdata(year1 + "年" + month1 + "月" + dayOfMonth1 + "日");


        return true;
    }



}
