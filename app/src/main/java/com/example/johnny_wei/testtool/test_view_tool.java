package com.example.johnny_wei.testtool;

import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

public class test_view_tool extends AppCompatActivity {
    String classname = getClass().getSimpleName();
    Activity mActivity = this;
    CheckBox checkBox1;
    CheckBox checkBox2;
    CheckBox checkBox3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_viewtool);

        setup_checkbox();
    }


    void setup_checkbox()
    {
        checkBox1 = (CheckBox)findViewById(R.id.checkBox1);
        checkBox2 = (CheckBox)findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox)findViewById(R.id.checkBox3);

        checkBox1.setOnCheckedChangeListener(checkBoxOnCheckedChange);
        checkBox2.setOnCheckedChangeListener(checkBoxOnCheckedChange);
        checkBox3.setOnCheckedChangeListener(checkBoxOnCheckedChange);


    }

    private CompoundButton.OnCheckedChangeListener checkBoxOnCheckedChange =
            new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { //buttonView 為目前觸發此事件的 CheckBox, isChecked 為此 CheckBox 目前的選取狀態
                    Log.d(classname,"onCheckedChanged");
                    switch (buttonView.getId()){
                        case R.id.checkBox1:
                            if(isChecked) {
                                Toast.makeText(mActivity, "checkBox1", Toast.LENGTH_SHORT).show();
                                Log.d(classname,"checkBox1");
                            } else{
                                Toast.makeText(mActivity, "uncheck checkBox1", Toast.LENGTH_SHORT).show();
                                Log.d(classname,"uncheck checkBox1");
                            }
                            break;
                        case R.id.checkBox2:
                            if(isChecked) {
                                Toast.makeText(mActivity, "checkBox2", Toast.LENGTH_SHORT).show();
                                Log.d(classname,"checkBox1");
                            } else{
                                Toast.makeText(mActivity, "uncheck checkBox2", Toast.LENGTH_SHORT).show();
                                Log.d(classname,"uncheck checkBox2");
                            }
                            break;
                        case R.id.checkBox3:
                            if(isChecked) {
                                Toast.makeText(mActivity, "checkBox3", Toast.LENGTH_SHORT).show();
                                Log.d(classname,"checkBox3");
                            } else{
                                Toast.makeText(mActivity, "uncheck checkBox3", Toast.LENGTH_SHORT).show();
                                Log.d(classname,"uncheck checkBox3");
                            }
                            break;
                    }

                }
            };
}
