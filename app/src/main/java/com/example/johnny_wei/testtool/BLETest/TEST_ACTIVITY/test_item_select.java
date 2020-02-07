package com.example.johnny_wei.testtool.BLETest.TEST_ACTIVITY;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.johnny_wei.testtool.R;
import com.example.johnny_wei.testtool.adapter.RowItem;
import com.example.johnny_wei.testtool.adapter.customAdapter;

import java.util.ArrayList;
import java.util.List;

public class test_item_select extends AppCompatActivity {

    //adapter
    ListView listView;
    List<RowItem> rowItems;
    customAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_item_select);

        setupAdapter();

        //read test file and show
        show_test_file();
    }

    void uninit() {
        rowItems.clear();
    }

    @Override
    protected void onDestroy() {
        uninit();
        super.onDestroy();
    }

    void setupAdapter() {
        listView = (ListView) findViewById(R.id.test_item_list);
        rowItems = new ArrayList<>();
        mAdapter = new customAdapter(this, rowItems);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Item " + (position + 1) + ": " + rowItems.get(position).getstrL2(),
                        Toast.LENGTH_SHORT);
                toast.show();

                //finish and back to calling activity
                finish(rowItems.get(position).getstrL1());
            }
        });
    }

    /**go back to previous activity*/
    public void finish(String filename) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("EXTRAS_DEVICE_FILENAME", filename);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    void show_test_file() {
        //read config
        //gen fake file for test
        int idx = 0;
        for (idx = 1; idx < 10; idx++) {
            RowItem rowItem = new RowItem("Test:" + idx, "", "");
            rowItems.add(rowItem);
        }

        //update list view
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                (mAdapter).notifyDataSetChanged();
            }
        });
    }

}
