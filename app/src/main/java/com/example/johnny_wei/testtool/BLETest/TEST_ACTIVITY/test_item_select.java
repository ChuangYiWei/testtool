package com.example.johnny_wei.testtool.BLETest.TEST_ACTIVITY;

import android.app.Activity;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.johnny_wei.testtool.R;
import com.example.johnny_wei.testtool.adapter.RowItem;
import com.example.johnny_wei.testtool.adapter.customAdapter;

import java.util.ArrayList;
import java.util.List;

public class test_item_select extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    //adapter
    ListView listView;
    List<RowItem> rowItems;
    customAdapter mAdapter;
    ConstraintLayout consView;
    LinearLayout LinView;

    String[] fakeItem = {"2.1", "2.2", "2.3"};
    List<CheckBox> checkBoxes_list = new ArrayList<>();

    private List<String> test = new ArrayList<String>();;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_item_select);
        consView=findViewById(R.id.cons_0);
        LinView=consView.findViewById(R.id.Linear_0_1);

        for(int i = 0; i < 30; i++) {
            CheckBox ch = new CheckBox(this);
            checkBoxes_list.add(ch);
            ch.setId(i);
            //ch.setText(fakeItem[i]);
            ch.setText("item"+i);
            ch.setOnCheckedChangeListener(checkBoxOnCheckedChange);
            LinView.addView(ch);
        }
        //read test file and show

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
//        resultIntent.putExtra("EXTRAS_DEVICE_FILENAME", filename);
        resultIntent.putStringArrayListExtra("test", (ArrayList<String>) test);
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
                        Toast.makeText(getApplicationContext(), buttonView.getText() + " 被選取", Toast.LENGTH_LONG).show();
                        Log.w(TAG,"add:"+tmpStr);
                        test.add(tmpStr);
                    } else {
                        Log.w(TAG,"remove:"+tmpStr);
                        test.remove(tmpStr);
                        Toast.makeText(getApplicationContext(), buttonView.getText() + " 被取消", Toast.LENGTH_LONG).show();
                    }
                    //buttonView.getId() == R.id.checkBox_0(等於所選到的那個)
                    Log.d(TAG, "buttonView.getId():" + buttonView.getId());

                }
            };


    public void sel_ok(View view) {
        back2Actvity();
    }
}
