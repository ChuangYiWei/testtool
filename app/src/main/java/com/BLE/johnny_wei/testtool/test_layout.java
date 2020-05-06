package com.BLE.johnny_wei.testtool;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class test_layout extends AppCompatActivity {

    EditText ed1;
    private Button mButton;
    private ConstraintLayout mConstraintLayout;
    ConstraintLayout mIncludeConstraintLayout;
    private Context mContext;
    private PopupWindow mPopupWindow;


    TextView textOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_layout);
        textOut = (TextView)findViewById(R.id.textout);
        popup();

        RadioButton programButton = (RadioButton) findViewById(R.id.program);
        RadioButton uiButton = (RadioButton) findViewById(R.id.ui);
        programButton.setOnCheckedChangeListener(mOnCheckedChangeListener);
        uiButton.setOnCheckedChangeListener(mOnCheckedChangeListener);

//        showlayoutdialog();
//        showEditdialog();
    }

    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.program:
                    Toast.makeText(test_layout.this, "安安, 程式設計師!", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.ui:
                    Toast.makeText(test_layout.this, "安安, UI設計師!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

//    public void onSelect(View view){
//        switch(view.getId()){
//            case R.id.program:
//                Toast.makeText(test_layout.this, "安安, 程式設計師!", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.ui:
//                Toast.makeText(test_layout.this, "安安, UI設計師!", Toast.LENGTH_SHORT).show();
//                break;
//        }
//    }

    void showEditdialog() {
        AlertDialog.Builder editDialog = new AlertDialog.Builder(test_layout.this);
        editDialog.setTitle("--- Edit ---");

        final EditText editText = new EditText(test_layout.this);
        editText.setText(textOut.getText());
        editDialog.setView(editText);

        editDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            // do something when the button is clicked
            public void onClick(DialogInterface arg0, int arg1) {
                textOut.setText(editText.getText().toString());
            }
        });
        editDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            // do something when the button is clicked
            public void onClick(DialogInterface arg0, int arg1) {
                //...
            }
        });
        editDialog.show();
    }


    void showlayoutdialog() {
        LayoutInflater inflater = LayoutInflater.from(test_layout.this);
        final View v = inflater.inflate(R.layout.devinfo, null);
        TextView tv = (v.findViewById(R.id.tv_dev));
        tv.setText("johnny");
        new AlertDialog.Builder(test_layout.this)
                .setTitle("請輸入你的id")
                .setView(v)
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        EditText editText = (EditText) (v.findViewById(R.id.editText1));
//                        Toast.makeText(getApplicationContext(), "你的id是" + editText.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }
    void showListdialog()
    {
        final String[] dinner = {"1\n2\n3\n","雞蛋糕","沙威瑪","澳美客","麵線","麵疙瘩"};

        AlertDialog.Builder dialog_list = new AlertDialog.Builder(test_layout.this);
        dialog_list.setTitle("利用List呈現");
        dialog_list.setItems(dinner, new DialogInterface.OnClickListener(){
            @Override
            //只要你在onClick處理事件內，使用which參數，就可以知道按下陣列裡的哪一個了
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Toast.makeText(test_layout.this, "你選的是" + dinner[which], Toast.LENGTH_SHORT).show();
            }
        });
        dialog_list.show();
    }


    void showdialog()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(test_layout.this);
        dialog.setTitle("基本訊息對話按鈕");
        dialog.setMessage("基本訊息對話功能介紹");
        dialog.setNegativeButton("NO",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                Toast.makeText(test_layout.this, "我還尚未了解",Toast.LENGTH_SHORT).show();
            }

        });
        dialog.setPositiveButton("YES",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                Toast.makeText(test_layout.this, "我了解了",Toast.LENGTH_SHORT).show();
            }

        });
        dialog.setNeutralButton("取消",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                Toast.makeText(test_layout.this, "取消",Toast.LENGTH_SHORT).show();
            }

        });
        dialog.show();
    }

    private void popup() {
        // Get the application context
        //mContext = getApplicationContext();
        mContext = this;
        mConstraintLayout = (ConstraintLayout) findViewById(R.id.parentCS);
        mButton = (Button) findViewById(R.id.btn);

        mButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Initialize a new instance of LayoutInflater service

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

                // Inflate the custom layout/view
                View customView = inflater.inflate(R.layout.custom_layout,null);

                // Initialize a new instance of popup window
                if(mPopupWindow == null) {
                    mPopupWindow = new PopupWindow(
                            customView,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT
                    );
                }

//                if(Build.VERSION.SDK_INT>=21){
//                    mPopupWindow.setElevation(5.0f);
//                }

                if(null == mPopupWindow)
                {
                    Log.e("jjj", "mPopupWindow is null");
                }
                try {
                    TextView tv = (TextView) customView.findViewById(R.id.tv);
                    String str = "THIS　is Friday Night !!!!";
                    tv.setText(str);
                    tv.setTextColor(0xffaabb00);
                    tv.setTextSize(20);
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Dismiss the popup window
                            Log.e("jjj", "dismiss mPopupWindow ");
                            mPopupWindow.dismiss();
                        }
                    });

                    // Finally, show the popup window at the center location of root relative layout

                    mPopupWindow.showAtLocation(mConstraintLayout, Gravity.CENTER, 0, 0);
                }catch (Exception e)
                {
                    Log.e("jjj", e.toString());
                }
            }
        });
    }

    void fromIncludeLayout()
    {
        ed1 = findViewById(R.id.ed_test1);
        String txt = ed1.getText().toString();
        Log.d("test_layout","edit txt is:" + txt);
    }

    public void clk_include(View view) {
        //find included layout
        View includedlayout = (ConstraintLayout)findViewById(R.id.inc1);
        includedlayout.setVisibility(View.VISIBLE);

        //get obj from included layout
        EditText tx = includedlayout.findViewById(R.id.ed_test1);
        Log.d("jjj","tx from sublayout:" + tx.getText().toString());
    }
}
