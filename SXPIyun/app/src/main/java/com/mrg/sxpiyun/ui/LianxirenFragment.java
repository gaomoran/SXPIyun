package com.mrg.sxpiyun.ui;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mrg.sxpiyun.adpter.Lianxiren2Adpter;
import com.zxg.sxpi.ui.R;
import com.mrg.sxpiyun.db.LianXiRenBD;
import com.mrg.sxpiyun.modle.LianXiRenMod;
import com.mrg.sxpiyun.util.GetName;
import com.mrg.sxpiyun.util.LianxirenList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LianxirenFragment extends Fragment {

    ExpandableListView lianxirenlist;
    private LianXiRenBD lianxirendb;
    private List<String> groupname=new ArrayList<String>();
    private HashMap<String,List<LianXiRenMod>> lianxiren;
    private Lianxiren2Adpter lianxiren2Adpter;
    private BroadcastReceiver myRecaiver;
    private AsyncTask<Void, String, Void> asyncTask;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_lianxiren, container, false);
        TextView textView = (TextView) view.findViewById(R.id.tv_top_title);
        textView.setText("联系人");
        RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.rl_lianxiren_adduser);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HaoYouSouSuoActivity.class);
                startActivity(intent);
            }
        });
        lianxirenlist = (ExpandableListView)view.findViewById(R.id.elv_lianxiren);
        lianxirendb=new LianXiRenBD(getActivity());
        groupname = lianxirendb.getLianxirenGroup();
        lianxiren = lianxirendb.getLianxirenList();
        lianxiren2Adpter = new Lianxiren2Adpter(getActivity(), groupname, lianxiren);
        lianxirenlist.setAdapter(lianxiren2Adpter);

        lianxirenlist.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String username = lianxiren.get(groupname.get(groupPosition)).get(childPosition).getUsername();
                Intent intent = new Intent(getActivity(), LiaotianActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);

                return true;
            }
        });
       /* asyncTask.execute();
        //注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("ShuaXiLianXiRenList");
        filter.addAction("LoginSuccess");
        myRecaiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("ShuaXiLianXiRenList")) {
                    shuaxinlist();
                } else if (intent.getAction().equals("LoginSuccess")){
                    System.out.println("收到广播==加载联系人");


                }

            }
        };

        getActivity(). registerReceiver(myRecaiver, filter);

        asyncTask = new AsyncTask<Void, String, Void>() {

           @Override
           protected Void doInBackground(Void... params) {

               groupname = lianxirendb.getLianxirenGroup();
               lianxiren = lianxirendb.getLianxirenList();
               publishProgress("1");
               lianxiren2Adpter = new Lianxiren2Adpter(getActivity(), groupname, lianxiren);
               publishProgress("2");
               return null;
           }

           @Override
           protected void onProgressUpdate(String... values) {
               if (values[0].equals("2")) {

                   lianxirenlist.setAdapter(lianxiren2Adpter);
                   shuaxinlist();
               }
               if (values[0].equals("1")) {
                   lianxirenlist.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                       @Override
                       public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                           String username = lianxiren.get(groupname.get(groupPosition)).get(childPosition).getUsername();
                           Intent intent = new Intent(getActivity(), LiaotianActivity.class);
                           intent.putExtra("username", username);
                           startActivity(intent);

                           return true;
                       }
                   });


               }


           }


       };*/


        lianxirenlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {

                System.out.println("长按");
                new AlertDialog.Builder(getActivity())
                        .setTitle("删除联系人")
                        .setMessage("是否要删除联系人")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.out.println("确认删除联系人");
                                TextView ssusername = (TextView) view.findViewById(R.id.tv_lianxiren_nicheng);
                                String finderrname = ssusername.getText().toString();
                                System.out.println("查询结果：" + lianxirendb.getUsername(finderrname));
                                String username = GetName.getUsername(lianxirendb.getUsername(finderrname));
                                //从服务器中删除联系人
                                LianxirenList.removeUser(username);
                                //从本地数据库中删除联系人
                                lianxirendb.removeUser(GetName.getName(username));

                                shuaxinlist();

                            }
                        })
                        .setNegativeButton("否", null)
                        .show();


                return true;
            }
        });


        return view;

    }

    /**
     * 刷新适配器
     */
    public void shuaxinlist(){
        System.out.println("刷新联系人链表");
        groupname=lianxirendb.getLianxirenGroup();
        lianxiren=lianxirendb.getLianxirenList();
        lianxiren2Adpter.ShuaxinLianxirenList(groupname, lianxiren);




    }

   /* @Override
    public void onDestroy() {
        super.onDestroy();
       getActivity().unregisterReceiver(myRecaiver);
    }*/
}
