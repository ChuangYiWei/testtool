package com.example.johnny_wei.testtool;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class customview extends AppCompatActivity {
    ListView listView;
    List<RowItem> rowItems;
    customAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customview);

        setupView();
    }

    private void setupView() {
        listView = (ListView) findViewById(R.id.id_list);
        rowItems = new ArrayList<RowItem>();
        mAdapter = new customAdapter(this, rowItems);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Item " + (position + 1) ,
                        Toast.LENGTH_SHORT);

            }
        });
    }

    public void clk_set(View view) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RowItem item = new RowItem("1","2","3");
                rowItems.add(item);
                mAdapter.notifyDataSetChanged();
            }
        });
    }


}
