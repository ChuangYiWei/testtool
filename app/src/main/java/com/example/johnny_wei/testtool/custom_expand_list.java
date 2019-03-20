package com.example.johnny_wei.testtool;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.johnny_wei.testtool.adapter.CustomExpandableListAdapter;
import com.example.johnny_wei.testtool.adapter.Item;
import com.example.johnny_wei.testtool.adapter.ServiceGroup;
import com.example.johnny_wei.testtool.adapter.charaItem;

import java.util.ArrayList;
import java.util.List;


public class custom_expand_list extends AppCompatActivity {
    private Context mContext;
    private ExpandableListView list_expand;

    private List<ServiceGroup> ServiceGroupData;
    private List<List<charaItem>> charaItemData;
    private List<charaItem> lData;

    private CustomExpandableListAdapter myAdapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_expand_list);

        mContext = custom_expand_list.this;

        list_expand = (ExpandableListView) findViewById(R.id.list_expand);

        ServiceGroupData = new ArrayList<ServiceGroup>();
        charaItemData = new ArrayList<List<charaItem>>();

        ServiceGroupData.add(new ServiceGroup("Battery","0x180F"));
        ServiceGroupData.add(new ServiceGroup("2Battery","0x190F"));

        lData = new ArrayList<charaItem>();
        charaItem charitem;
        charitem = new charaItem("Battery level","0x2A19",0,33,1);
        lData.add(charitem);
        charitem = new charaItem("2Battery level","0x3A19",1,43,2);
        lData.add(charitem);
        charaItemData.add(lData);

        lData = new ArrayList<charaItem>();
        charitem = new charaItem("3Battery level","0x4A19",2,53,3);
        lData.add(charitem);
        charitem = new charaItem("4Battery level","0x5A19",3,63,4);
        lData.add(charitem);
        charaItemData.add(lData);

        myAdapter = new CustomExpandableListAdapter(ServiceGroupData,charaItemData,mContext);
        list_expand.setAdapter(myAdapter);

        list_expand.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(mContext, "你點了：" + charaItemData.get(groupPosition).get(childPosition).GetCharaName(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}
