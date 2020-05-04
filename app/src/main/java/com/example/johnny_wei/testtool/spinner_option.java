package com.example.johnny_wei.testtool;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.johnny_wei.testtool.BLETest.TEST_ACTIVITY.testcase_select;

import static com.example.johnny_wei.testtool.config.globalConfig.EXTRAS_TEST_CASE;
import static com.example.johnny_wei.testtool.config.globalConfig.REQ_CODE_TEST_ITEM_ACT;

public class spinner_option extends AppCompatActivity {
    LayoutInflater inflater;
    AlertDialog.Builder builder;
    View dialogView;
    LinearLayout linerView;
    View scrollView;
    ConstraintLayout constraintView;
    private final String TAG = getClass().getSimpleName();
    Activity mActivity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner_option);//使用Spinner
        //setupSpinner();

    }

    void setupSpinner()
    {
        Spinner notifySpinner = (Spinner)findViewById(R.id.notify_spinner);
        String[] testModeArray = {"SPI mode","UART mode", "AIR UART CMD mode", "AIR HCI CMD mode"};
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.myspinner, testModeArray);

        adapter.setDropDownViewResource(R.layout.myspinner);
        notifySpinner.setAdapter(adapter);
        notifySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){

                Toast.makeText(spinner_option.this, "You choose "+adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }
            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(spinner_option.this, "You did not choose", Toast.LENGTH_LONG).show();
            }
        });
    }

    RadioButton baudrate_115200;
    RadioButton baudrate_921600;
    //group
    private void setup_setting_baudrate() {
        linerView.findViewById(R.id.cons_02_baudrate).setVisibility(View.VISIBLE);
        View constraintView = (ConstraintLayout)linerView.findViewById(R.id.cons_02_baudrate);
        baudrate_115200 = constraintView.findViewById(R.id.con02_radio_115200);
        baudrate_921600 = constraintView.findViewById(R.id.con02_radio_921600);
        RadioGroup radioGroup_baudrate = constraintView.findViewById(R.id.con02_radioGr_baudrate);
        radioGroup_baudrate.setOnCheckedChangeListener(mOnCheckedChangeListener);
    }

    private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.con02_radio_115200:
                    Toast.makeText(mActivity, "115200 select", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.con02_radio_921600:
                    Toast.makeText(mActivity, "921600 select", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    void setup_setting_Spinner()
    {
        linerView.findViewById(R.id.cons03_csv).setVisibility(View.VISIBLE);
        View constraintView = (ConstraintLayout)linerView.findViewById(R.id.cons03_csv);
        Spinner notifySpinner = (Spinner)constraintView.findViewById(R.id.con03_spin_csv);
        final String[] Baudrate_Array = getResources().getStringArray(R.array.csv_file_list);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.myspinner, Baudrate_Array);

        adapter.setDropDownViewResource(R.layout.myspinner);
        notifySpinner.setAdapter(adapter);
        notifySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                Toast.makeText(spinner_option.this, "You choose "+adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                Log.d(TAG,"select file is:" + Baudrate_Array[position]);
            }
            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(spinner_option.this, "You did not choose", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 設置要用哪個menu檔做為選單
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("jjj","onOptionsItemSelected");
        // 取得點選項目的id
        int id = item.getItemId();

        // 依照id判斷點了哪個項目並做相應事件
        if (id == R.id.action_settings) {
            // 按下「設定」要做的事
            inflater = this.getLayoutInflater();
            linerView = (LinearLayout) inflater.inflate(R.layout.test_linear, null);
            //測試把view關掉
            linerView.findViewById(R.id.Cons_00_showlog).setVisibility(View.VISIBLE);
            linerView.findViewById(R.id.line_00).setVisibility(View.VISIBLE);
            //自己新增
            TextView m1 = new TextView(this);
            String str= "linerView added 1";
            m1.setText(str);
            m1.setTextColor(Color.RED);
            //m1.setTextColor(getResources().getColor(R.color.color_status, null));
            linerView.addView(m1);

            TextView m11 = new TextView(this);
            String str11= "linerView added 11";
            m11.setText(str11);
            linerView.addView(m11);

            CheckBox cb = new CheckBox(this);
            cb.setText("Tutlane");
            cb.setChecked(true);
            linerView.addView(cb);

            /* add fake text view, can't scroll if we don't use scroll view*/
            /*
            int i=0;
            for(i=0;i<10;i++)
            {
                TextView m = new TextView(this);
                String s= "linerView added"+i;
                m.setText(s);
                linerView.addView(m);
            }
            */

            constraintView = (ConstraintLayout)linerView.findViewById(R.id.cons_02_baudrate);

            //constraint layout自己新增layout不會像linear一樣排好
//            TextView m2 = new TextView(this);
//            String str2= "constraintView added 1";
//            m2.setText(str2);
//            constraintView.addView(m2);
            final EditText edt = linerView.findViewById(R.id.Cons_00_showlog).findViewById(R.id.cons_00__edt);
            edt.setText("please input");
            builder = new AlertDialog.Builder(this);
            builder.setTitle("這是標題")
                    .setView(linerView)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Log.d(TAG,"input text is:"+ edt.getText().toString());
                            //do something
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();

            setup_setting_Spinner();
            setup_setting_baudrate();

            Toast.makeText(this, "設定", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (id == R.id.action_help) {
            // 按下「使用說明」要做的事
            //setupSpinner();
            Toast.makeText(this, "使用說明", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (id == R.id.action_scroll) {
            // 按下「使用說明」要做的事
            //TODO add test item in scroll
            inflater = this.getLayoutInflater();
            scrollView = (View) inflater.inflate(R.layout.test_scroll, null);
            linerView = scrollView.findViewById(R.id.scroll_linear);
            constraintView = linerView.findViewById(R.id.scroll_linear_cons_1);
            Spinner notifySpinner = constraintView.findViewById(R.id.dbglevel_spinner);
            String[] testModeArray = {"INFO","DEBUG"};
            ArrayAdapter adapter = new ArrayAdapter(this, R.layout.myspinner, testModeArray);

            adapter.setDropDownViewResource(R.layout.myspinner);
            notifySpinner.setAdapter(adapter);
            notifySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
                public void onItemSelected(AdapterView adapterView, View view, int position, long id){

                    Toast.makeText(spinner_option.this, "You choose "+adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                }
                public void onNothingSelected(AdapterView arg0) {
                    Toast.makeText(spinner_option.this, "You did not choose", Toast.LENGTH_LONG).show();
                }
            });

//            /* add fake text view, can't scroll if we don't use scroll view*/
//            int i=0;
//            for(i=0;i<30;i++)
//            {
//                TextView m = new TextView(this);
//                String s= "linerView added"+i;
//                m.setText(s);
//                linerView.addView(m);
//            }
//
            builder = new AlertDialog.Builder(this);
            builder.setTitle("")
                    .setView(scrollView)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do something
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
//            Toast.makeText(this, "幫忙", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (id == R.id.baud_rate) {
            // 按下「使用說明」要做的事
            Toast.makeText(this, "baud rate", Toast.LENGTH_SHORT).show();
            inflater = this.getLayoutInflater();
            dialogView = inflater.inflate(R.layout.test_aleart, null);
            builder = new AlertDialog.Builder(this);
            builder.setTitle("這是標題")
                    .setView(dialogView)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do something
                        }
                    });
            AlertDialog dialog = builder.create();
//        AlertDialog dialog = (AlertDialog) createAddPersonDialog();
            dialog.show();

            Toast.makeText(this, "baud_rate", Toast.LENGTH_SHORT).show();

            return true;
        }
        else if (id == R.id.test_file) {
            Intent go2intent = new Intent(spinner_option.this, testcase_select.class);
            spinner_option.this.startActivityForResult(go2intent, REQ_CODE_TEST_ITEM_ACT);
        }
        return super.onOptionsItemSelected(item);

    }


//user implement this to check
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.w(TAG,"requestCode:"+requestCode);
        //request code is back from the class we call
        if(requestCode == 1  && resultCode == RESULT_OK){
            Log.w(TAG,"open bluetooth OK");
        }else if(requestCode == REQ_CODE_TEST_ITEM_ACT && resultCode == RESULT_OK){
            Log.w(TAG,"test item name:"+data.getExtras().getStringArrayList(EXTRAS_TEST_CASE));
        }
        else if(requestCode == 1 && resultCode == RESULT_CANCELED){
            Toast.makeText(spinner_option.this,"please open bluetooth !! ",Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
