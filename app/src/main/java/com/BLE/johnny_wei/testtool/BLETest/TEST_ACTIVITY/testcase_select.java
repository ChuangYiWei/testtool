package com.BLE.johnny_wei.testtool.BLETest.TEST_ACTIVITY;

import android.app.Activity;
import android.content.Intent;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.BLE.johnny_wei.testtool.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.BLE.johnny_wei.testtool.config.globalConfig.EXTRAS_TEST_CASE;
import static com.BLE.johnny_wei.testtool.config.globalConfig.map_testcase;

public class testcase_select extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();


    //layout
    ConstraintLayout constraintView;
    LinearLayout linearLayout;
    ScrollView scrollView;
    String[] fakeItem = {"2.1", "2.2", "2.3", "2.4", "2.5",
                         "2.6", "2.7", "2.8", "2.9", "2.10"};
    LinkedHashMap<String, String> m_map_testcase = new LinkedHashMap<String, String>();
    List<CheckBox> checkBoxes_list = new ArrayList<>();

    private List<String> list_testcase = new ArrayList<String>();
    //TODO:define key in global
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testcase_select);

        setup_view();
        init_map_testcase();
        //set list_testcase case
        for (Map.Entry<String, String> entry : m_map_testcase.entrySet()) {
            System.out.println("key= " + entry.getKey() + ", value=" + entry.getValue());
            CheckBox ch = new CheckBox(this);
            checkBoxes_list.add(ch);
            //use hashmap key to add case
            ch.setText(entry.getKey());
            ch.setOnCheckedChangeListener(checkBoxOnCheckedChange);
            linearLayout.addView(ch);
        }
    }


    void init_map_testcase()
    {
        m_map_testcase = map_testcase;
//        m_map_testcase.put("2.3.1 xxx","1.csv");
//        m_map_testcase.put("2.3.2 xxx","2.csv");
//        m_map_testcase.put("2.3.9 Loopack","loopback.csv");

    }

    void genFakeItem(){
        for(int i = 0; i < fakeItem.length; i++) {
            CheckBox ch = new CheckBox(this);
            checkBoxes_list.add(ch);
            //use hashmap key to add case
            ch.setId(i);
            ch.setText(fakeItem[i]);
            ch.setOnCheckedChangeListener(checkBoxOnCheckedChange);
            linearLayout.addView(ch);
        }
    }
    void setup_view()
    {
        constraintView =findViewById(R.id.cons_0);
        scrollView =findViewById(R.id.sv_view);//for scroll
        linearLayout = scrollView.findViewById(R.id.Linear_testcase);
    }

    void uninit() {

    }

    @Override
    protected void onDestroy() {
        uninit();
        super.onDestroy();
    }

    /**go back to previous activity*/
    public void back2Actvity() {
        Intent resultIntent = new Intent();
        resultIntent.putStringArrayListExtra(EXTRAS_TEST_CASE, (ArrayList<String>) list_testcase);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    //設定callback
    private CompoundButton.OnCheckedChangeListener checkBoxOnCheckedChange =
            new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { //buttonView 為目前觸發此事件的 CheckBox, isChecked 為此 CheckBox 目前的選取狀態
                    String tmpStr = buttonView.getText().toString();
                    if (isChecked)//等於 buttonView.isChecked()
                    {
                        //Toast.makeText(getApplicationContext(), buttonView.getText() + " 被選取", Toast.LENGTH_LONG).show();
                        Log.w(TAG, "add:" + tmpStr);
                        list_testcase.add(tmpStr);
                    } else {
                        Log.w(TAG, "remove:" + tmpStr);
                        list_testcase.remove(tmpStr);
                        //Toast.makeText(getApplicationContext(), buttonView.getText() + " 被取消", Toast.LENGTH_LONG).show();
                    }
                    //buttonView.getId() == R.id.checkBox_0(等於所選到的那個)
                    Log.d(TAG, "buttonView.getId():" + buttonView.getId());

                }
            };


    public void clk_testcase_ok(View view) {
        back2Actvity();
    }

    public void clk_toggle(View view) {
        for(int i = 0; i < m_map_testcase.size(); i++) {
            checkBoxes_list.get(i).setChecked(!checkBoxes_list.get(i).isChecked());
        }
    }
}
