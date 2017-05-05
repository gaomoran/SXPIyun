package com.mrg.sxpiyun.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.search.ReportedData;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.jivesoftware.smackx.xdata.Form;


import android.app.ProgressDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zxg.sxpi.ui.R;
import com.mrg.sxpiyun.adpter.HaoYouSouSuoAdpter;
import com.mrg.sxpiyun.modle.QueryHaoyouMod;
import com.mrg.sxpiyun.util.ConnectionAdmin;
import com.mrg.sxpiyun.util.GetName;

import java.util.ArrayList;
import java.util.List;

public class HaoYouSouSuoActivity extends AppCompatActivity {
    private EditText edit_addfriend;
    private Button btn_searchfriend;
    private HaoYouSouSuoAdpter haoYouSouSuoAdpter;
    private String name,password,response,acceptAdd,alertName,alertSubName;
    private ImageView img_searchFriend,img_addFriend;
    private TextView text_searchFriend,text_response;
    private ListView lv_query_result;
    private Roster roster=ConnectionAdmin.roster;
    private XMPPConnection con = ConnectionAdmin.conns;
    private static ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hao_you_sou_suo);
        TextView textView = (TextView)findViewById(R.id.tv_top_title);
        textView.setText("添加好友");


        edit_addfriend = (EditText) findViewById(R.id.edit_addfriend);
        btn_searchfriend = (Button) findViewById(R.id.btn_searchfriend);
        lv_query_result = (ListView) findViewById(R.id.lv_query_result);
        haoYouSouSuoAdpter = new HaoYouSouSuoAdpter(this);
        lv_query_result.setAdapter(haoYouSouSuoAdpter);

        btn_searchfriend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                haoYouSouSuoAdpter.quertuser(
                        sousuoUser(edit_addfriend.getText().toString()));

            }
        });

        lv_query_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView ssusername=(TextView)view.findViewById(R.id.tv_sousuohaoyou_username);

                String finderrname=ssusername.getText().toString();

                System.out.println("要加的好友："+finderrname);
                if(addFriend(finderrname)){
                    Toast.makeText(getApplicationContext(),"发送成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(),"添加失败",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }



        //添加好友

        public  boolean addFriend(String friendName) {

        try {
            System.out.println("正在添加的好友" + GetName.getUsername(friendName));
            roster.createEntry(GetName.getUsername(friendName), friendName, new String[]{"Friends"});
            System.out.println("添加好友成功！！");
            return true;
        } catch (Exception e) {
            e.printStackTrace();

            System.out.println("失败！！" + e);
            return false;
        }
    }




    /**
     * 向服务器查询好友
     * @param username 用户名
     * @return 用户名集合
     */
    public List<QueryHaoyouMod> sousuoUser(String username) {
        List<QueryHaoyouMod> haoyoulist = new ArrayList<QueryHaoyouMod>();
        try {
            //查询管理器
            UserSearchManager usm = new UserSearchManager(ConnectionAdmin.conns);

            Form searchForm = usm.getSearchForm("search." + ConnectionAdmin.conns.getServiceName());
            Form answerForm = searchForm.createAnswerForm();
            answerForm.setAnswer("Username", true);
            answerForm.setAnswer("search", username);
            ReportedData data = usm.getSearchResults(answerForm, "search." + ConnectionAdmin.conns.getServiceName());
            //被查询的链表
            List<ReportedData.Row> it = data.getRows();
            //查询结果

            for (ReportedData.Row row : it) {
                String haoyouname = row.getValues("Username").get(0);
                VCard vCard=new VCard();
                vCard.load(ConnectionAdmin.conns,GetName.getUsername(haoyouname));
                String haoyounicheng=vCard.getNickName();
                QueryHaoyouMod haoyouxixi=new QueryHaoyouMod();
                System.out.print("查询好友结果：" + haoyouname + "好友昵称：" + haoyounicheng + "\n");
                haoyouxixi.setUsername(haoyouname);
                haoyouxixi.setNick(haoyounicheng);
                haoyoulist.add(haoyouxixi);
            }

        } catch (Exception e) {
            Toast.makeText(this, "查询失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return haoyoulist;
    }

    public void top_backward(View v){
        finish();
    }
}
