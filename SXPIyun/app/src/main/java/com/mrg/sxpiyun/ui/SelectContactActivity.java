package com.mrg.sxpiyun.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zxg.sxpi.ui.R;
import com.mrg.sxpiyun.adpter.SelectContactadpter;
import com.mrg.sxpiyun.db.LianXiRenBD;
import com.mrg.sxpiyun.modle.QueryHaoyouMod;

import java.util.List;

public class SelectContactActivity extends Activity {

    private ListView listView;
    private LianXiRenBD lianXiRenBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact);
        listView = (ListView) findViewById(R.id.lv_selectcontact_contact);
        lianXiRenBD = new LianXiRenBD(this);
        List<QueryHaoyouMod> lxrnicheng=lianXiRenBD.getLXRnichengAll();
        listView.setAdapter(new SelectContactadpter(getApplicationContext(),lxrnicheng));
        TextView textView = (TextView)findViewById(R.id.tv_top_title);
        textView.setText("选择联系人");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView nicheng = (TextView) view.findViewById(R.id.tv_selectcontact_nicheng);
                String nichen = nicheng.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("nichen",nichen);
                setResult(1, intent);
                finish();

            }
        });
    }
    public void top_backward(View v){
        finish();
    }
}
